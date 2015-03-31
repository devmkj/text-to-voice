package ttm.service;

/**
 * Created by shibaprasad on 2/4/2015.
 */
public interface TTsWatcher {

    public void onStart(String utteranceId);
    public void onDone(String utteranceId);
    public void onError(String utteranceId);
}
