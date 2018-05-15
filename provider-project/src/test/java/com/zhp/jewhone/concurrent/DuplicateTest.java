package com.zhp.jewhone.concurrent;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class DuplicateTest {


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ExecutorService exec = Executors.newCachedThreadPool();
        // thread_num个线程可以同时访问
        int thread_num = 30;
        final Semaphore semp = new Semaphore(thread_num);
        // 模拟2000个客户端访问
        int client_num = 30;
        for (int index = 0; index < client_num; index++) {
            exec.execute(new TaskThread(semp, index));
        }
        long timeSpend = (System.currentTimeMillis()-start);
        System.out.println( "花费1: "+timeSpend +"秒" );
        // 退出线程池
        exec.shutdown();
        timeSpend = System.currentTimeMillis()-start;
        System.out.println( "花费2: "+timeSpend +"秒" );

    }


    static class TaskThread implements Runnable {
        Semaphore semp;
        int NO;


        TaskThread(Semaphore semp, int NO) {
            this.semp = semp;
            this.NO = NO;
        }


        @Override
        public void run() {
            try {
// 获取许可
                semp.acquire();
                System.out.println("Thread:" + NO);
// String host =
// "http://10.99.23.42:7001/KMQueryCenter/query.do?";
// String para = "method=getQueryResult&pageNum=1&pageSize=5&" +
// "queryKeyWord=" + getRandomSearchKey(NO)
// + "&questionID=-1&questionIdPath=-1&searchType=1" +
// "&proLine=&proSeries=&proType=" + NO;


// http://192.168.2.115:8080/revitbus/revit/list.html?&tagId=12-&type=0&system=1&year=2016
                String host = "http://192.168.2.10:8080/revitbus/revit/list.html?";
                String para = "&type=0&system=1&year=2016";


                System.out.println(host + para);
                URL url = new URL(host);// 此处填写供测试的url
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(para);
                out.flush();
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
// System.out.println(result);
// Thread.sleep((long) (Math.random()) * 1000);
// 释放
                System.out.println("第：" + NO + " 个");
//System.out.println(result);
                semp.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}