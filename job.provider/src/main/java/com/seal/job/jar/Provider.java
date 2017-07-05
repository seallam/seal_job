package com.seal.job.jar;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.seal.job.executor.SpiderSimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hasee on 2017/6/26.
 */
public class Provider {
    private static final Logger logger = LoggerFactory.getLogger(Provider.class);
    private static volatile boolean running = true;

    public static void main(final String[] args) {
        try {
            String profile = "development";
            if (args != null && args.length > 0) {
                profile = args[0];
            }
            System.setProperty("spring.profiles.active", profile);
            new ClassPathXmlApplicationContext("conf/seal-job-context.xml");
            new JobScheduler(getRegCenter(), createJobConfiguration()).init();
            logger.info(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " service server started!");
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            System.exit(1);
        }
//        synchronized (Main.class) {
//            while (running) {
//                try {
//                    Provider.class.wait();
//                } catch (Throwable e) {
//                }
//            }
//        }
        com.alibaba.dubbo.container.Main.main(args);
    }

    private static CoordinatorRegistryCenter getRegCenter() {
        ZookeeperConfiguration config = new ZookeeperConfiguration("47.88.171.155:2181", "seal-job");
        CoordinatorRegistryCenter registryCenter = new ZookeeperRegistryCenter(config);
        registryCenter.init();
        return registryCenter;
    }

    private static LiteJobConfiguration createJobConfiguration() {
        // 创建作业配置
        JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder("sealSimpleJob", "0/10 * * * * ?", 1).shardingItemParameters("0=0").build();
        DataflowJobConfiguration dataflowJobConfig = new DataflowJobConfiguration(coreConfig, SpiderSimpleJob.class.getCanonicalName(), true);
        LiteJobConfiguration result = LiteJobConfiguration.newBuilder(dataflowJobConfig).build();
        return result;
    }
}
