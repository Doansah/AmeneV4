package com.embabel.template.agent;

import java.util.List;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.api.models.OpenAiModels;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.template.agent.Domain.KnowledgeGraph;



public class KnowledgeGraphBuilder {
    private  TopicGeneratorAgent topicGeneratorAgent;
    
    @Action(description = "Define the relationships between a list of topics from Central Node")
    public List<Domain.RelationEdge> defineRelationsFromCentralNode(List<Domain.TopicNode> topics,                                                          
                                                                    OperationContext context) {
        var promptRunner = context.ai().withLlm(LlmOptions.withModel(OpenAiModels.GPT_5_MINI)
            .withTemperature(0.8));

        Domain.TopicNode centralNode = topicGeneratorAgent.findCentralNode(topics);

            return (List<Domain.RelationEdge>) promptRunner.createObject(
             """
                You are an expert in educational content structuring. Given the following list of topics and a central topic node, 
                define the relationships between the central node and the other topics to form a coherent knowledge structure.

                Central Topic Node:
                %s

                Other Topics:
                %s

                Instructions:
                1. Identify how each topic relates to the central node (e.g., prerequisite, related concept, advanced topic).
                2. Create clear relationship types for each connection.
                3. Ensure the relationships support a logical learning progression.

                Output a list of relation edges in the format:
                - From Topic ID
                - To Topic ID
                - Relation Type
                """.formatted(centralNode.toString(), topics.toString()),
                Domain.RelationEdge.class
            ); 

    }
    
    // Does not ensure that each TopicNode is connected to Central Node
    @Action(description="Define the relationships between a list of topics")
    public List<Domain.RelationEdge> defineTopicRelations(List<Domain.TopicNode> topics, OperationContext context) {
        var promptRunner = context.ai().withLlm(LlmOptions.withModel(OpenAiModels.GPT_5_MINI)
            .withTemperature(0.8));

            return (List<Domain.RelationEdge>) promptRunner.createObject(
             """
                You are an expert in educational content structuring. Given the following list of topics, 
                define the relationships between them to form a coherent knowledge structure.

                Topics:
                %s

                Instructions:
                1. Identify how each topic relates to others (e.g., prerequisite, related concept, advanced topic).
                2. Create clear relationship types for each connection.
                3. Ensure the relationships support a logical learning progression.

                Output a list of relation edges in the format:
                - From Topic ID
                - To Topic ID
                - Relation Type
                """.formatted(topics.toString()),
                Domain.RelationEdge.class
            ); 

    }

    @Action(description = "Build a knowledge graph from topics and relations")
    @AchievesGoal(description = "Create a structured knowledge graph representing topics and their relationships")
    public KnowledgeGraph buildKnowledgeGraph(OperationContext context, Domain.User user) {
        List<Domain.TopicNode> topics = topicGeneratorAgent.generateRelatedTopics(user, context);

        var promptRunner = context.ai().withLlm(LlmOptions.withModel(OpenAiModels.GPT_5_MINI)
            .withTemperature(0.8));

            return promptRunner.createObject(
             """
                You are an expert in knowledge graph construction. Given the following list of topics, 
                create a structured knowledge graph that includes a central topic node and defines 
                relationships between the topics.

                Topics:
                %s

                Instructions:
                1. Identify the most relevant topic to be the central node based on its importance and connections.
                2. Define clear relationships between topics (e.g., prerequisite, related concept, advanced topic).
                3. Ensure the graph is well-structured for educational purposes.

                Output the knowledge graph in a structured format including:
                - Central Topic ID
                - List of Topic Nodes with their details
                - List of Relation Edges defining connections between topics
                """.formatted(topics.toString()),
                KnowledgeGraph.class
            ); 
    }
 
    
}