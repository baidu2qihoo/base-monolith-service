package com.hugh.base.service.raft.atomix;

import io.atomix.cluster.Member;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.profile.ConsensusProfile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Atomix node demo that starts local Atomix instance using bootstrap discovery.
 * Note: Requires Atomix dependencies and may need additional transport configuration.
 */
public class AtomixDemoMain {

    public static Atomix startNode(String nodeId, int port, String storageDir, List<Member> members) {
        Atomix atomix = Atomix.builder()
                .withMember(Member.builder(nodeId).withHost("127.0.0.1").withPort(port).build())
                .withMembershipProvider(BootstrapDiscoveryProvider.builder().withNodes(members).build())
                .withProfile(ConsensusProfile.builder().build())
                .withStorage(Paths.get(storageDir))
                .build();
        atomix.start().join();
        System.out.println("Atomix node started: " + nodeId);
        return atomix;
    }

    public static void main(String[] args) throws Exception {
        // Example: args = node1 5000 storage/node1 node1:5000,node2:5001,node3:5002
        if (args.length < 4) {
            System.out.println("Usage: AtomixDemoMain <nodeId> <port> <storageDir> <nodesCsv>"); 
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
        startNode(nodeId, port, storage, members);
        Thread.currentThread().join();
    }
}
