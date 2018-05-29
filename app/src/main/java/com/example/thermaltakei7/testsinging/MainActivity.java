package com.example.thermaltakei7.testsinging;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;


import com.example.thermaltakei7.testsinging.pitcheswords.MakePitchesList;
import com.example.thermaltakei7.testsinging.pitcheswords.MakeWordsList;
import com.example.thermaltakei7.testsinging.pitcheswords.Pitch;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final static double GRAPH_STARTING_X = -0.8;
    final static double GRAPH_ENDING_X = 0.4;
    List<java.util.List<Pitch>> wordsList;
    private Viewport graphViewPort;
    private Handler handler;
    private Runnable runnable;
    private GraphView graph;
    private DataPoint[] points;
    private LineGraphSeries<DataPoint> series;
    private int seriesNr;
    private double orderNr = GRAPH_STARTING_X;
    private ImageButton playRecordButton;
    private List<Pitch> pitchesList;
    private int songIndex;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Textview and Button
        playRecordButton = (ImageButton) findViewById(R.id.playRecord);

        //Initialize pitches and words
        MakePitchesList listOfPithes = new MakePitchesList();
        MakeWordsList listOfWords = new MakeWordsList();
        pitchesList = listOfPithes.getListOfPitches();
        wordsList = listOfWords.getWordsList(pitchesList);

        //Initialize graph
        graph = (GraphView) findViewById(R.id.graph);
        initGraph();

        //ViewPort
        graphViewPort = graph.getViewport();

        //Handler
        handler = new Handler();

        playRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPlaying){
                    playMedia(songIndex);

                    //Start moving graph
                    //Moved to PlayMedia - setOnPreparedListen
//                    drawAndMoveGraph();

                    isPlaying = true;
                } else if(isPlaying){

                    //Move graph to starting position
                    resetGraph();

                    //Stop Playing audio
                    stopAudio();

                    isPlaying = false;

                }
            }
        });


    }

    //Moves the graph every 100ms by 0.1 which results in 1 every second
    private void drawAndMoveGraph() {
        runnable = new Runnable() {
            public void run() {
                graphViewPort.setMinX(orderNr);
                graphViewPort.setMaxX(orderNr + 1);
                graph.invalidate();
                if (pitchesList.size() != orderNr) {
                    orderNr = orderNr + 0.1;
                }


                handler.postDelayed(this, 100);
            }
        };
        runnable.run();
    }

    //Load up and paint the graph
    private void initGraph() {
        for (int i = 0; i < wordsList.size(); i++) {
            seriesNr = 0;
            points = new DataPoint[wordsList.get(i).size()];
            for (Pitch pitch : wordsList.get(i)) {
                points[seriesNr] = new DataPoint(pitch.getOccuranceTime(), pitch.getPitch());
                seriesNr++;
            }
            series = new LineGraphSeries<>(points);
            series.setThickness(15);
            graph.addSeries(series);
        }

        //VocalTestPoints - Just for vocal testing
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 50),
                new DataPoint(0, 75),
                new DataPoint(0, 100),
                new DataPoint(0, 125),
                new DataPoint(0, 150),
                new DataPoint(0, 175),
                new DataPoint(0, 200),
                new DataPoint(0, 225),
                new DataPoint(0, 250),
                new DataPoint(0, 275),
        });
        series.setSize(5);
        series.setColor(Color.YELLOW);
        graph.addSeries(series);


        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-50);
        graph.getViewport().setMaxY(400);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(GRAPH_STARTING_X);
        graph.getViewport().setMaxX(GRAPH_ENDING_X);
        graph.getViewport().setScrollable(true);
    }


//    Play the chosen song and start moving the graph
    private void playMedia(int songIndex) {

        Uri uri = Uri.parse("android.resource://com.example.thermaltakei7.testsinging/" + R.raw.perfect);

        mediaPlayer = new MediaPlayer();

        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                drawAndMoveGraph();
            }
        });


    }

    //Stop audio from playing
    private void stopAudio() {
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    //Reset graph to its initial position
    private void resetGraph() {
        handler.removeCallbacks(runnable);
        graphViewPort.setMinX(GRAPH_STARTING_X);
        graphViewPort.setMaxX(GRAPH_ENDING_X);
        graph.invalidate();
        orderNr = GRAPH_STARTING_X;
    }


}
