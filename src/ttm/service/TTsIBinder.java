package ttm.service;

import android.os.Binder;

public class TTsIBinder extends Binder {

    private TTSService ttsService;

    public TTsIBinder(TTSService ttsService) {
        this.ttsService = ttsService;
    }

    public TTSService getTTsService() {
        return this.ttsService;
    }
}
