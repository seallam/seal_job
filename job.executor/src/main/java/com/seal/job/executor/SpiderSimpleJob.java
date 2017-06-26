package com.seal.job.executor;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.seal.job.executor.abstractJob.AbstractJob;

/**
 * Created by Hasee on 2017/6/26.
 */
public class SpiderSimpleJob extends AbstractJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {

    }
}
