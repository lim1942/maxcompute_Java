package com.aliyun.odps.mapred.open.example;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;

import java.io.IOException;
public class Sleep {
    private static final String SLEEP_SECS = "sleep.secs";
    public static class MapperClass extends MapperBase {
        //由于没有输入数据，map函数不会被执行，相关逻辑只能写在setup中。
        @Override
        public void setup(TaskContext context) throws IOException {
            try {
                //获取jobconf中设置的sleep秒数，进行sleep。
                Thread.sleep(context.getJobConf().getInt(SLEEP_SECS, 1) * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: Sleep <sleep_secs>");
            System.exit(-1);
        }
        JobConf job = new JobConf();
        job.setMapperClass(MapperClass.class);
        //这个实例也是一个MapOnly的，所以需要设置reducer个数为0。
        job.setNumReduceTasks(0);
        //由于没有输入表，mapper的个数需要由用户明确进行指定。
        job.setNumMapTasks(1);
        job.set(SLEEP_SECS, args[0]);
        JobClient.runJob(job);
    }
}