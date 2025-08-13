package com.hugh.base.service.raft.atomix;

import io.atomix.core.Atomix;
import io.atomix.core.profile.ConsensusProfile;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.cluster.Member;
import io.atomix.cluster.discovery.NodeDiscoveryProvider;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider.Builder;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

/**
 * Example Atomix node starter. This is a sample demonstrating how to create an Atomix instance.
 * For production you should provide proper transport and storage directories, and handle lifecycle.
 */
public class AtomixStarter {

    public static Atomix buildNode(String nodeId, List<Member> members, String storageDir) {
        Atomix.Builder builder = Atomix.builder()
                .withMember(Member.builder(nodeId)
                        .withHost("127.0.0.1")
                        .withPort(5000)
                        .build())
                .withMembershipProvider(BootstrapDiscoveryProvider.builder()
                        .withNodes(members)
                        .build())
                .withProfile(ConsensusProfile.builder().build())
                .withStorage(Paths.get(storageDir));

        return builder.build();
    }
}
