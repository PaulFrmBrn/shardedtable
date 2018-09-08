package com.paulfrmbrn.sharded.table.dao.shrading;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;


public class ShardingService {

    private final RangeMap<Long, Shard> shards;

    public ShardingService(long minKeyValue, long maxKeyValue, List<Shard> shards) {

        requireNonNull(shards);

        if (shards.size() == 0) {
            throw new IllegalArgumentException("Shards size is 0");
        }

        ImmutableRangeMap.Builder<Long, Shard> builder = ImmutableRangeMap.builder();
        if (shards.size() == 1) {
            builder.put(Range.closed(minKeyValue, maxKeyValue), shards.get(0));
        } else {

            List<Shard> shardList = new ArrayList<>(shards);
            shardList.sort(Comparator.comparingInt(Shard::getNumber));

            long rangeLength = maxKeyValue - minKeyValue + 1;
            long subRangeLength = rangeLength / shards.size();
            long extraKeys = rangeLength % shards.size();

            long start = minKeyValue;
            long end = start + subRangeLength - 1 + extraKeys;

            for (int i = 0; i < shardList.size(); i++) {
                builder.put(Range.closed(start, end), shards.get(i));
                start = end + 1;
                end = start + subRangeLength - 1;
            }

        }
        this.shards = builder.build();

    }

    @Nonnull
    public Shard getShard(long key) {
        Shard shard = shards.get(key);
        if (shard == null) {
            throw new IllegalArgumentException("key is invalid");
        }
        return shard;
    }

}
