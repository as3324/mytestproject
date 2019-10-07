package com.example.demo.aqs;

/**
 * @Author: yej
 * @Date: 2019/9/23 19:31
 * @Version 1.0
 */

import com.example.demo.util.ComUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟多线程请求响应结果
 */
public class CompletableFutureTest1 {

    private GoodsDao goodsDao=new GoodsDao();
    private GoodsService goodsService=new GoodsService();

    public static void main(String[] args) {
        new CompletableFutureTest1().requestPool();

    }


    public void requestPool(){
        final int maxNum=10000;
        BlockingQueue<Runnable> blockingQueue=new ArrayBlockingQueue<Runnable>(maxNum);
        ThreadPoolExecutor executor=
                new ThreadPoolExecutor(100,maxNum,10, TimeUnit.MINUTES,blockingQueue);
        for(int i=0;i<maxNum;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Goods goods= new GoodsController().query(ComUtil.getRandomNumber(100).toString());
                    if(goods!=null){
                        System.out.println(goods.getId()+":"+goods.getName());
                    }
                }
            });
        }
    }

    interface Service<T>{
        public T query(String id);
    }

    interface Dao<T>{
        public List<T> queryBatch(Collection<String> ids);
    }


    class GoodsController{
        private Service<Goods> service=goodsService;


        public Goods query(String id){
            return this.service.query(id);
        }

    }

    class GoodsService implements Service<Goods>{
        private  BlockingQueue<Request> queue=new LinkedBlockingQueue<Request>();

        private  Dao dao=goodsDao;

        {
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    Set<String> ids=new HashSet<String>();
                    Map<String,List<CompletableFuture<Goods>>> futureMap=new HashMap<String,List<CompletableFuture<Goods>>>();
                    while(queue.size()>0){
                        try {
                            Request request= queue.take();
                            String id=request.getId();
                            ids.add(id);
                            List<CompletableFuture<Goods>> futureList=futureMap.get(id);
                            if(futureList==null){
                                futureList=new ArrayList<CompletableFuture<Goods>>();
                                futureMap.put(id,futureList);
                            }
                            futureList.add(request.getFuture());

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    List<Goods> goodsList= dao.queryBatch(ids);
                    Map<String,Goods> goodsMap=generatorGoodsMap(goodsList);
                    for(String id:ids){
                        List<CompletableFuture<Goods>> futures= findFuture(id,futureMap);
                        for(int j=0;j<futures.size();j++){
                            CompletableFuture<Goods> future= futures.get(j);
                            future.complete(goodsMap.get(id));
                        }
                    }
                }

            };
            Timer timer=new Timer();
            timer.schedule(task,1000,200);
        }

        private List<CompletableFuture<Goods>> findFuture(String id,Map<String,List<CompletableFuture<Goods>>> futureMap){
            return futureMap.get(id);

        }

        private Map<String, Goods> generatorGoodsMap(List<Goods> goodsList) {
            Map<String,Goods> goodsMap=new HashMap<String,Goods>();
            if(goodsList==null){
                return goodsMap;
            }

            for(Goods goods:goodsList){
                goodsMap.put(goods.getId(),goods);
            }
            return goodsMap;
        }

        @Override
        public Goods query(String id) {
            CompletableFuture<Goods> completableFuture=new CompletableFuture<Goods>();
            queue.offer(new Request(id,completableFuture));
            Goods result=null;
            try {
                result=completableFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    class GoodsDao implements Dao<Goods>{

        public Map<String,Goods> datas=new ConcurrentHashMap<String, Goods>();
        AtomicInteger atomicInteger=new AtomicInteger();
         {
            int n=100000;
            for(int i=0;i<n;i++){
                Goods goods=new Goods();
                goods.setId(String.valueOf(atomicInteger.incrementAndGet()));
                goods.setName(ComUtil.getRandomNumber(100000).toString());
                datas.put(goods.getId(),goods);
            }
        }

        @Override
        public List<Goods> queryBatch(Collection<String> ids) {
            List<Goods> goodsList=new ArrayList<Goods>();
            Iterator<String> iterator=ids.iterator();
            for(;iterator.hasNext();){
                String id= iterator.next();
                Goods goods= datas.get(id);
                if(goods!=null){
                    goodsList.add(goods);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return goodsList;
        }
    }

    class DatabaseConnection{
        public Connection getConnection() throws SQLException {
           return DriverManager.getConnection("");
        }
    }

    class Goods{
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class Request{
        private String id;
        private CompletableFuture<Goods> future;

        public Request(String id,CompletableFuture<Goods> future){
            this.id=id;
            this.future=future;

        }

        public String getId() {
            return id;
        }


        public CompletableFuture<Goods> getFuture() {
            return future;
        }

    }

}
