#!/bin/bash

run_test_case() {
  
    local QUEUE_TYPE=$1
    local DELIVERY_MODE=$2
    local TEST_NAME=$3
    
    echo "Running test case: $TEST_NAME"

    export QUEUE_TYPE=$QUEUE_TYPE
    export QUEUE_MODE=$DELIVERY_MODE

    # Create or update setenv.bat to set DELIVERY_MODE
    echo "Setting DELIVERY_MODE in setenv.bat..."
    echo "@echo off" > "$TOMCAT_PATH/bin/setenv.bat"
    echo "set CATALINA_OPTS=\"-DQUEUE_TYPE=$QUEUE_TYPE -DQUEUE_MODE=$DELIVERY_MODE\"" >> "$TOMCAT_PATH/bin/setenv.bat"
    
    
    # Define Tomcat path
    TOMCAT_PATH="/c/Program Files/Apache Software Foundation/Tomcat 11.0"
    
    # Check if Tomcat directory exists
    if [ ! -d "$TOMCAT_PATH" ]; then
        echo "Error: Tomcat directory not found at $TOMCAT_PATH"
        exit 1
    fi

 
    # Deploy mom.war to Windows Tomcat
    echo "Deploying mom.war to Tomcat..."
    cp mom.war "$TOMCAT_PATH/webapps/"
    "$TOMCAT_PATH/bin/startup.bat"
    sleep 10 # Give some time for Tomcat to start
    
    # Start consumers
    CONSUMER_PORT=10000 java -jar test-mom-jar-with-dependencies.jar consumer 1 $DELIVERY_MODE $QUEUE_TYPE news &
    C1_PID=$!
    CONSUMER_PORT=10001 java -jar test-mom-jar-with-dependencies.jar consumer 2 $DELIVERY_MODE $QUEUE_TYPE news notifications &
    C2_PID=$!
    CONSUMER_PORT=10002 java -jar test-mom-jar-with-dependencies.jar consumer 3 $DELIVERY_MODE $QUEUE_TYPE notifications &
    C3_PID=$!
    
    # Start producers
    java -jar test-mom-jar-with-dependencies.jar producer 1 $DELIVERY_MODE $QUEUE_TYPE news &
    P1_PID=$!
    java -jar test-mom-jar-with-dependencies.jar producer 2 $DELIVERY_MODE $QUEUE_TYPE news notifications &
    P2_PID=$!
    java -jar test-mom-jar-with-dependencies.jar producer 3 $DELIVERY_MODE $QUEUE_TYPE notifications &
    P3_PID=$!
    
    # Wait for processes to complete execution (or add a timeout if needed)
    wait $P1_PID $P2_PID $P3_PID
    sleep 3 # Give some time for consumers to consume messages
    
    # Kill consumers and core
    kill $C1_PID $C2_PID $C3_PID $CORE_PID
    echo "Test case $TEST_NAME completed."
    sleep 2 # Give some time for the core to stop

    # Shutdown Tomcat
    echo "Shutting down Tomcat..."
    "$TOMCAT_PATH/bin/shutdown.bat"
    sleep 10 # Give some time for Tomcat to shut down
}

# Run all test cases
run_test_case "PUBLISHER_SUBSCRIBER"  "PULL"  "Pub-Sub Pull-Based Queue"
run_test_case "PEER_TO_PEER"          "PUSH"  "P2P Push-Based Queue"
run_test_case "PUBLISHER_SUBSCRIBER"  "PUSH"  "Pub-Sub Push-Based Queue"
run_test_case "PEER_TO_PEER"          "PULL"  "P2P Pull-Based Queue"

echo "All test cases completed."
