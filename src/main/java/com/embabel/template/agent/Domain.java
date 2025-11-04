package com.embabel.template.agent;

import java.time.Instant;
import java.util.List;

import com.embabel.template.enums.DifficultyBand;
import com.embabel.template.enums.ImportanceBand;


public class Domain {

    public static class TopicNode {
        private String id;
        private String name;
        private String description;
        private String goal;
        private List<String> tags;
        private DifficultyBand difficultyBand;
        private ImportanceBand importanceBand;
        private double breadthBias;
        private double depthBias;

        public TopicNode(String id, String name, String description, String goal, 
                        List<String> tags, String difficultyBand, String importanceBand, 
                        double breadthBias, double depthBias) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.goal = goal;
            this.tags = tags;
            this.difficultyBand = DifficultyBand.valueOf(difficultyBand);
            this.importanceBand = ImportanceBand.valueOf(importanceBand);
            this.breadthBias = breadthBias;
            this.depthBias = depthBias;
        }
    }   

public static class RelationEdge {
        private String fromTopicId;
        private String toTopicId;
        private String relationType;

        public RelationEdge(String fromTopicId, String toTopicId, String relationType) {
            this.fromTopicId = fromTopicId;
            this.toTopicId = toTopicId;
            this.relationType = relationType;
        }
    }

public static class KnowledgeGraph { 
        private String centralTopicId;
        private List<TopicNode> topics;
        private List<RelationEdge> edges;
        private Instant createdAt;

        public KnowledgeGraph(String centralTopicId, List<TopicNode> topics, 
                             List<RelationEdge> edges, Instant createdAt) {
            this.centralTopicId = centralTopicId;
            this.topics = topics;
            this.edges = edges;
            this.createdAt = createdAt;
        }
    }

public static class PracticeItem {
        private String id;
        private String topicId;
        private String question;
        private String answer;
        private List<String> hints;
        private String explanation;
        private DifficultyBand difficultyBand;

        public PracticeItem(String id, String topicId, String question, String answer, 
                           List<String> hints, String explanation, String difficultyBand) {
            this.id = id;
            this.topicId = topicId;
            this.question = question;
            this.answer = answer;
            this.hints = hints;
            this.explanation = explanation;
            this.difficultyBand = DifficultyBand.valueOf(difficultyBand);
        }
    }
public static class User {
        private String id;
        private String name;
        private String email;
        private String currentTopic;
        private String learningGoal;

        public User(String id, String name, String email, String currentTopic, String learningGoal) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.currentTopic = currentTopic;
            this.learningGoal = learningGoal;
        }

        public String getCurrentTopic() {
            return this.currentTopic;
        }

        public String getLearningGoal() {
            return this.learningGoal;
        }
    }

}