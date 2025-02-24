import os
import json

def load_files(filename):
    with open(filename) as f:
        return f.read().splitlines()
    
def load_log(delivery_type, queue_type):
    print("loading logs for " + delivery_type + " " + queue_type)
    log_files = []
    for file in os.listdir("logs/" + delivery_type):
        if file.startswith(queue_type):
            log_files.append(load_files("logs/" + delivery_type + "/" + file))
    return log_files

def check_order_messages(messages):
    print("Checking order of messages")
    # Check if the messages are in order
    last_message_per_topic_per_producer = {}     
    for message in messages:
        topic = message["topic"]
        message_number = int(message["message"])
        producer = message["producer"]

        if topic not in last_message_per_topic_per_producer:
            last_message_per_topic_per_producer[topic] = {}
        if producer not in last_message_per_topic_per_producer[topic]:
            last_message_per_topic_per_producer[topic][producer] = -1
        
        if last_message_per_topic_per_producer[topic][producer] >= message_number:
            print("Messages are not in order")
            print("Topic: " + topic)
            print("Producer: " + producer)
            print("Last message: " + str(last_message_per_topic_per_producer[topic][producer]))
            print("Current message: " + str(message_number))

        last_message_per_topic_per_producer[topic][producer] = message_number

def check_duplicates(messages):
    print("Checking for duplicates")
    # Check for duplicates
    message_set = set()
    for message in messages:
        if str(message) in message_set:
            print("Duplicate message found")
            print(message)
        message_set.add(str(message))

def check_missing_messages(messages):
    print("Checking for missing messages")
    # Check for missing messages
    message_set_for_topic_for_producer = {}
    for message in messages:
        topic = message["topic"]
        message_number = int(message["message"])
        producer = message["producer"]

        if topic not in message_set_for_topic_for_producer:
            message_set_for_topic_for_producer[topic] = {}
        if producer not in message_set_for_topic_for_producer[topic]:
            message_set_for_topic_for_producer[topic][producer] = set()
        
        if len(message_set_for_topic_for_producer[topic][producer]) > 0 and message_number - 1 not in message_set_for_topic_for_producer[topic][producer]:
            print("Missing message found")
            print("Topic: " + topic)
            print("Producer: " + producer)
            print("Message: " + message)
            return
        message_set_for_topic_for_producer[topic][producer].add(message_number)

def check_duplicate_messages_in_different_consumers(consumers_list_message):
    print("Checking for duplicate messages in different consumers")
    # Check for duplicate messages in different consumers
    message_set = []
    for consumer in consumers_list_message:
        for message in consumer:
            if message in message_set:
                print("Duplicate message found")
                print(message)
            message_set.append(message)

def check_missing_messages_in_different_consumers(consumers_list_message):
    print("Checking for missing messages in different consumers")
    # Check for missing messages in different consumers
    message_set_for_topic_for_producer = {}
    messages_per_topic = 10
    for consumer in consumers_list_message:
        for message in consumer:
            topic = message["topic"]
            message_number = int(message["message"])
            producer = message["producer"]

            if topic not in message_set_for_topic_for_producer:
                message_set_for_topic_for_producer[topic] = {}
            if producer not in message_set_for_topic_for_producer[topic]:
                message_set_for_topic_for_producer[topic][producer] = set()

            message_set_for_topic_for_producer[topic][producer].add(message_number)

    
    for topic in message_set_for_topic_for_producer:
        for producer in message_set_for_topic_for_producer[topic]:
            if len(message_set_for_topic_for_producer[topic][producer]) < messages_per_topic:
                print("Missing messages found")
                print("Topic: " + topic)
                print("Expected messages: " + str(messages_per_topic))
                print("Actual messages: " + str(len(message_set_for_topic_for_producer[topic][producer])))

def check_pub_sub_messages(consumers_list_message):
    for consumer in consumers_list_message:
        check_order_messages(consumer)
        check_duplicates(consumer)
        check_missing_messages(consumer)

def check_peer_to_peer_messages(consumers_list_message):
    check_duplicate_messages_in_different_consumers(consumers_list_message)
    check_missing_messages_in_different_consumers(consumers_list_message)
    i = 1
    for consumer in consumers_list_message:
        print("Checking consumer " + str(i))
        check_order_messages(consumer)
        check_duplicates(consumer)
        i += 1
       
    
    
def to_consumer_list_message(log_files):
    consumers_list_message = []
    for log_file in log_files:
        messages = []
        for line in log_file:
            messages.append(json.loads(line))
        consumers_list_message.append(messages)
    return consumers_list_message

def main():
    logs = load_log("pull", "PEER_TO_PEER")
    consumer_list_message = to_consumer_list_message(logs)
    check_peer_to_peer_messages(consumer_list_message)

    logs = load_log("pull", "PUBLISHER_SUBSCRIBER")
    consumer_list_message = to_consumer_list_message(logs)
    check_pub_sub_messages(consumer_list_message)

    logs = load_log("push", "PEER_TO_PEER")
    consumer_list_message = to_consumer_list_message(logs)
    check_peer_to_peer_messages(consumer_list_message)

    logs = load_log("push", "PUBLISHER_SUBSCRIBER")
    consumer_list_message = to_consumer_list_message(logs)
    check_pub_sub_messages(consumer_list_message)



main()