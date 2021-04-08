package dk.easv;

import javafx.concurrent.Task;

public class SlideshowTask extends Task {

    private int time;
    private int size;
    private int startIndex;

    public void setTime(int time) {
        this.time = time;
    }

    public void setNumberOfImages(int size) {
        this.size = size;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    protected Object call() throws Exception {
        int i = startIndex;
        while(true) {
            if(isCancelled()) return 0;

            updateValue(i);
            Thread.sleep(time*1000);
            i++;
            if(i == size) i = 0;
        }
    }
}
