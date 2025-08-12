#!/bin/bash
set -e
ROOT_DIR=$(pwd)

# Build modules
mvn -T 1C -DskipTests package

# Generate RSA keys if missing
KEYS_DIR=monolith-core/keys
mkdir -p $KEYS_DIR
if [ ! -f $KEYS_DIR/private.pem ]; then
  echo "Generating RSA keypair (2048) for demo..."
  openssl genpkey -algorithm RSA -out $KEYS_DIR/private.pem -pkeyopt rsa_keygen_bits:2048
  openssl rsa -pubout -in $KEYS_DIR/private.pem -out $KEYS_DIR/public.pem
  # create base64 of DER public key
  openssl rsa -pubin -in $KEYS_DIR/public.pem -pubout -outform DER | base64 > $KEYS_DIR/public.b64
fi

export JWT_PUBLIC_KEY_B64=$(cat $KEYS_DIR/public.b64)

# Start Atomix demo nodes in background (3 nodes)
NODES_CSV=node1:5000,node2:5001,node3:5002
mkdir -p storage/node1 storage/node2 storage/node3
java -cp monolith-core/target/monolith-core-1.0.0.jar com.hugh.base.service.raft.atomix.AtomixLeaderDemo node1 5000 storage/node1 $NODES_CSV &
sleep 2
java -cp monolith-core/target/monolith-core-1.0.0.jar com.hugh.base.service.raft.atomix.AtomixLeaderDemo node2 5001 storage/node2 $NODES_CSV &
sleep 2
java -cp monolith-core/target/monolith-core-1.0.0.jar com.hugh.base.service.raft.atomix.AtomixLeaderDemo node3 5002 storage/node3 $NODES_CSV &
sleep 5

# Start monolith app node (single instance for demo)
java -jar monolith-app/target/monolith-app-0.0.1-SNAPSHOT.jar --node.id=node1 --node.members=node1,node2,node3 &
APP_PID=$!
sleep 6

# Call health endpoint via curl
echo "Calling health endpoint..."
curl -s http://localhost:8080/api/app/health || true

# Query Atomix counter (demo printed to stdout by Atomix nodes)
echo "Demo complete. Check Atomix node logs for leader/counter info."

# Stop app
kill $APP_PID || true
