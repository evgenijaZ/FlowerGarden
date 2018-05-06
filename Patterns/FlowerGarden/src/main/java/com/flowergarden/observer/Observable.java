package com.flowergarden.observer;

public interface Observable<O, N> {
    void addObserver(O observer);

    void removeObserver(O observer);

    void notifyObservers(N notification);
}
