package io.audio.openacall;

import android.app.Application;
import io.audio.openacall.model.CurrentUserSettings;
import io.audio.openacall.model.WorkerThread;
import io.audio.openacall.rtmtutorial.ChatManager;

public class AGApplication extends Application {

    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }
    private ChatManager mChatManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mChatManager = new ChatManager(this);
        mChatManager.init();
    }

    public ChatManager getChatManager() {
        return mChatManager;
    }

    private static AGApplication sInstance;

    public static AGApplication the() {
        return sInstance;
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    public static final CurrentUserSettings mAudioSettings = new CurrentUserSettings();
}
