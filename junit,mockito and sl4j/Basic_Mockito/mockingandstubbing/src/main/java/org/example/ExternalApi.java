package org.example;

/**
 * Represents an external API dependency. In production this would call an
 * external system; in tests we will mock/stub this interface.
 */
public interface ExternalApi {
    String getData();
}

