package com.flowergarden.factories;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;

public interface AbstractFlowerFactory {
     Rose createRose();
     Chamomile createChamomile();
}
