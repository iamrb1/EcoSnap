package edu.msu.cse476.baragurr.ecosnap;

import java.util.Collections;
import java.util.List;

public class VisionRequest {
    public List<Request> requests;

    public VisionRequest(Request request) {
        this.requests = Collections.singletonList(request);
    }

    public static class Request {
        public Image image;
        public List<Feature> features;

        public Request(String base64Image) {
            this.image = new Image(base64Image);
            this.features = Collections.singletonList(new Feature("LABEL_DETECTION", 10));
        }
    }

    public static class Image {
        public String content;

        public Image(String content) {
            this.content = content;
        }
    }

    public static class Feature {
        public String type;
        public int maxResults;

        public Feature(String type, int maxResults) {
            this.type = type;
            this.maxResults = maxResults;
        }
    }
}

