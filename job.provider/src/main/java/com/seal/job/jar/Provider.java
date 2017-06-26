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

/**
 * Created by Hasee on 2017/6/26.
 */
public class Provider {
    private static Logger logger = LoggerFactory.getLogger(Provider.class);

    public static void main(String[] args) {
        new JobScheduler(getRegCenter(), createJobConfiguration()).init();
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
