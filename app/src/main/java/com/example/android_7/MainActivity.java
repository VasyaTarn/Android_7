package com.example.android_7;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    int iterations = 100;
    int delay = 300;
    ProgressBar indicatorBar;
    TextView statusView, taskStatusView;
    Button btnStartStop, btnCancel;
    ProgressTask progressTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        indicatorBar = findViewById(R.id.indicator);
        statusView = findViewById(R.id.statusView);
        taskStatusView = findViewById(R.id.taskStatusView);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnCancel = findViewById(R.id.btnCancel);

        btnStartStop.setOnClickListener(v -> {
            if (progressTask == null || progressTask.getStatus() == AsyncTask.Status.FINISHED)
            {
                startTask();
            }
            else
            {
                stopTask();
            }
        });

        btnCancel.setOnClickListener(v -> cancelTask());

        btnCancel.setEnabled(false);
    }

    private void startTask()
    {
        indicatorBar.setProgress(0);
        statusView.setText("Status: 0%");
        taskStatusView.setText("Status: Pending");

        progressTask = new ProgressTask();
        progressTask.execute();

        btnStartStop.setText("Stop");
        btnCancel.setEnabled(true);
        taskStatusView.setText("Status: Running");
    }

    private void stopTask()
    {
        if (progressTask != null)
        {
            progressTask.cancel(true);
        }

        resetUIAfterStop();
    }

    private void cancelTask()
    {
        if (progressTask != null)
        {
            progressTask.cancel(true);
        }

        taskStatusView.setText("Status: Canceled");
        btnCancel.setEnabled(false);
    }

    private void resetUIAfterStop()
    {
        btnStartStop.setText("Start");
        btnCancel.setEnabled(false);
    }

    class ProgressTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... unused)
        {
            for (int i = 0; i <= iterations; i++)
            {
                if (isCancelled()) break;

                publishProgress(i * 100 / iterations);
                SystemClock.sleep(delay);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress)
        {
            indicatorBar.setProgress(progress[0]);
            statusView.setText("Status: " + progress[0] + "%");
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            Toast.makeText(MainActivity.this, "Task completed", Toast.LENGTH_SHORT).show();
            taskStatusView.setText("Status: Finished");
            resetUIAfterStop();
        }

        @Override
        protected void onCancelled()
        {
            Toast.makeText(MainActivity.this, "Task canceled", Toast.LENGTH_SHORT).show();
            taskStatusView.setText("Status: Canceled");
            resetUIAfterStop();
        }
    }
}