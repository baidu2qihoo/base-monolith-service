#!/bin/bash
# Example startup for 3-node cluster with Atomix demo and JWT env
# Start nodes with Atomix demo in background and monolith-app
NODES_CSV=node1:5000,node2:5001,node3:5002

# Start Atomix nodes (for demo only)
mkdir -p storage/node1 storage/node2 storage/node3
java -cp monolith-core/target/monolith-core-1.0.0.jar com.hugh.base.service.raft.atomix.AtomixDemoMain node1 5000 storage/node1 $NODES_CSV &
java -cp monolith-core/target/monolith-core-1.0.0.jar com.hugh.base.service.raft.atomix.AtomixDemoMain node2 5001 storage/node2 $NODES_CSV &
java -cp monolith-core/target/monolith-core-1.0.0.jar com.hugh.base.service.raft.atomix.AtomixDemoMain node3 5002 storage/node3 $NODES_CSV &

# Example JWT public key env (base64 X.509)
export JWT_PUBLIC_KEY_B64="$(cat keys/public.b64 2>/dev/null || echo '')"

# Start monolith app nodes
java -jar monolith-app/target/monolith-app-0.0.1-SNAPSHOT.jar --node.id=node1 --node.members=node1,node2,node3 &
java -jar monolith-app/target/monolith-app-0.0.1-SNAPSHOT.jar --node.id=node2 --node.members=node1,node2,node3 &
java -jar monolith-app/target/monolith-app-0.0.1-SNAPSHOT.jar --node.id=node3 --node.members=node1,node2,node3 &
