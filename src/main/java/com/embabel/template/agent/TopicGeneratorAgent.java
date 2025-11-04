package com.embabel.template.agent;

// This class is responsible for generating topics based on user input and learning goals

import java.util.List;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.api.models.OpenAiModels;
import com.embabel.agent.core.CoreToolGroups;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.template.agent.Domain.TopicNode;
import com.embabel.template.agent.Domain.User;
import com.embabel.template.enums.NodeType;

public class TopicGeneratorAgent {


    @Action(description = "Generate topics based on user input and learning goals", toolGroups = CoreToolGroups.WEB)
    @AchievesGoal(description = "Generate relevant topics based on user learning topic and goals")
    //Topics related to a user's desired learning topic and learning goal
    public List<TopicNode> generateRelatedTopics(User user, OperationContext context) { 
        String desiredTopic = user.getCurrentTopic();
        String userGoal = user.getLearningGoal();


        var promptRunner = context.ai().withLlm(LlmOptions.withModel(OpenAiModels.GPT_5_MINI)
            .withTemperature(0.8));


        return (List<TopicNode>) promptRunner.createObjectIfPossible(
             """
                You are an expert educational content curator. Generate a comprehensive list of learning topics 
                related to the user's current topic: "%s" and their learning goal: "%s".

                For each topic, provide:
                - A clear, descriptive name
                - A brief description of what the topic covers
                - Relevant tags for categorization
                - Difficulty level (BEGINNER, INTERMEDIATE, ADVANCED)
                - Importance level (LOW, MEDIUM, HIGH, CRITICAL)

                Include:
                1. Prerequisites needed before studying the main topic
                2. Core concepts within the main topic
                3. Advanced applications and extensions
                4. Related fields and cross-disciplinary connections

                Generate 8-12 well-structured topics that form a coherent learning path.
                """.formatted(desiredTopic, userGoal),
                TopicNode.class
            );
        
    }
    // Generate the central topic node based on user input
    @Action(description = "Generate the central topic node based on user input")
    public TopicNode generateCentralTopicNode(User user, OperationContext context) {
        String desiredTopic = user.getCurrentTopic();
        String userGoal = user.getLearningGoal();

        var promptRunner = context.ai().withLlm(LlmOptions.withModel(OpenAiModels.GPT_5_MINI)
            .withTemperature(0.8));

        return (TopicNode) promptRunner.createObjectIfPossible(
            """
                You are an expert educational content curator. Generate a central topic node based on the user's current topic: "%s" and their learning goal: "%s".

                The central topic node should include:
                - A clear, descriptive name
                - A brief description of what the topic covers
                - Relevant tags for categorization
                - Difficulty level (BEGINNER, INTERMEDIATE, ADVANCED)
                - Importance level (LOW, MEDIUM, HIGH, CRITICAL)

                Include:
                1. Prerequisites needed before studying the main topic
                2. Core concepts within the main topic
                3. Advanced applications and extensions
                4. Related fields and cross-disciplinary connections

                Generate a well-structured central topic node.
                """.formatted(desiredTopic, userGoal),
            TopicNode.class
        );
    }
    // for now just assume there only one central node 
    // no majors yet!
    public TopicNode findCentralNode(List<TopicNode> nodes) {
        for (TopicNode node : nodes) {
            if (node.nodeType == NodeType.CENTRAL) {
                return node;
            }
        }

        return null;
    }
}