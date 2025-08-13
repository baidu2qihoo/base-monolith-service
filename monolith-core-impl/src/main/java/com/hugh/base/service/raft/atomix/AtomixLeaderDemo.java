package com.hugh.base.service.raft.atomix;

import io.atomix.core.Atomix;
import io.atomix.core.election.LeaderElector;
import io.atomix.core.counter.Counter;
import io.atomix.core.profile.ConsensusProfile;
import io.atomix.cluster.Member;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Leader election and distributed counter demo using Atomix primitives.
 * Run multiple instances with different nodeId/port and same nodesCsv to form cluster.
 */
public class AtomixLeaderDemo {

    public static Atomix buildAndStart(String nodeId, int port, String storageDir, List<Member> members) {
        Atomix atomix = Atomix.builder()
                .withMember(Member.builder(nodeId).withHost("127.0.0.1").withPort(port).build())
                .withMembershipProvider(BootstrapDiscoveryProvider.builder().withNodes(members).build())
                .withProfile(ConsensusProfile.builder().build())
                .withStorage(Paths.get(storageDir))
                .build();
        atomix.start().join();
        return atomix;
    }

    public static void main(String[] args) throws Exception {
        // args: nodeId port storage nodesCsv
        if (args.length < 4) {
            System.out.println("Usage: AtomixLeaderDemo <nodeId> <port> <storageDir> <nodesCsv>");
            return;
        }
        String nodeId = args[0];
        int port = Integer.parseInt(args[1]);
        String storage = args[2];
        String csv = args[3];
        List<Member> members = new ArrayList<>();
        for (String s : csv.split(",")) {
            String[] p = s.split(":");
            members.add(Member.builder(p[0]).withHost("127.0.0.1").withPort(Integer.parseInt(p[1])).build());
        }

        Atomix atomix = buildAndStart(nodeId, port, storage, members);

        // Leader elector
        LeaderElector elector = atomix.getLeaderElector("demo-election");
        elector.run().thenAccept(leadership -> {
            System.out.println("Acquired leadership: " + leadership.leader());
            // start leader work: increment distributed counter periodically
            Counter counter = atomix.getCounter("demo-counter");
            counter.incrementAndGet().thenAccept(v -> System.out.println("Leader incremented counter to: " + v));
        });

        // Periodically print current leader and counter
        Counter counter = atomix.getCounter("demo-counter");
        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    String leader = elector.getLeadership().leader() != null ? elector.getLeadership().leader().id().id() : "none";
                    long value = counter.get().join();
                    System.out.println("Current leader=" + leader + " counter=" + value);
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).join();
    }
}
