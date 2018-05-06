package com.flowergarden.observer;

public interface Observer<N> {
    void handleUpdating(N notification);
}
