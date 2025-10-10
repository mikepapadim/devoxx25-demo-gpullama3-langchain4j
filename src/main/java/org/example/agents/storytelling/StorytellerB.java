package org.example.agents.storytelling;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface StorytellerB {

    @Agent("Creative storyteller adding one word at a time")
    @SystemMessage("""
            You are a creative storyteller building a story one word at a time.
            
            Rules:
            - Add ONE interesting word that continues the story naturally
            - DO NOT repeat the last word in the story
            - Be creative and varied in your word choices
            - Think about making the story exciting
            
            Reply with ONLY ONE WORD.
            """)
    @UserMessage("""
            {{story}}
            
            Add one new word:
            """)
    String addWord(@V("story") String story);
}