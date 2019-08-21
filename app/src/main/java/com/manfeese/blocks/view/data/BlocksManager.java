package com.manfeese.blocks.view.data;

import com.manfeese.blocks.view.draw.controller.DrawManager;

public class BlocksManager {

    private DrawManager drawManager;

    public BlocksManager() {
        drawManager = new DrawManager();
    }

    public DrawManager drawer() {
        return drawManager;
    }

}
