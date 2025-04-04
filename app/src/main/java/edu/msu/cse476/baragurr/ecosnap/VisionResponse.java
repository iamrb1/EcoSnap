package edu.msu.cse476.baragurr.ecosnap;

import java.util.List;

public class VisionResponse {
    public List<Response> responses;

    public static class Response {
        public List<LabelAnnotation> labelAnnotations;
    }

    public static class LabelAnnotation {
        public String description;
        public float score;
    }
}
