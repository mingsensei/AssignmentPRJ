package org.example.rf.model;

import java.util.Set;

public class CompletionStatus {
    private boolean courseCompleted;
    private Set<Long> completedChapterIds;
    private Set<Long> completedLessonIds;

    public CompletionStatus(boolean courseCompleted, Set<Long> completedChapterIds, Set<Long> completedLessonIds) {
        this.courseCompleted = courseCompleted;
        this.completedChapterIds = completedChapterIds;
        this.completedLessonIds = completedLessonIds;
    }

    public boolean isCourseCompleted() { return courseCompleted; }
    public Set<Long> getCompletedChapterIds() { return completedChapterIds; }
    public Set<Long> getCompletedLessonIds() { return completedLessonIds; }
}

