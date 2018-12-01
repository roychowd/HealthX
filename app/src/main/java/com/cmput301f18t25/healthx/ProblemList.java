/*
 *  * Copyright (c) Team 25, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 *
 */
package com.cmput301f18t25.healthx;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProblemList {
    /**
     * Creates a singleton class instance of ProblemList
     */
    private static ProblemList instance;
//    private static final ProblemList ourInstance = new ProblemList();

    private static ArrayList<Problem> problemArray;
    private static User user;

    /**
     Static method that creates instance of Singleton ProblemListclass
     */
    public static ProblemList getInstance() {
        if (instance == null) {
            instance = new ProblemList();
        }

        return instance;
    }

    /**
     * Returns problem at a given index
     * @param index - index of problem you want to return
     * */

    private ProblemList() {
        problemArray = new ArrayList<Problem>();

    }


    public  ArrayList<Problem> getProblemArray() {
        sortArray();
        return problemArray;
    }

    public void setProblemArray(ArrayList<Problem> array) {

        problemArray = array;
        sortArray();
    }

    public  User getUser() {
        return user;
    }

    public void setUser(User user) {
        ProblemList.user = user;
    }

    /**
     * Adds a problem to ProblemList
     * @param problem problem to be added
     * */
    public  void addToProblemList(Problem problem) {
        //Problem  newProblem = new Problem(problem.getTitle(), problem.getDescription(), problem.getDate());
        problemArray.add(problem);
        sortArray();
    }
    /**
     * Edits a problem in ProblemList
     * @param e the new edited problem to insert into list
     * @param index the index of problem you wish to edit
     * */
    // Edits a specific problem -- Note the function takes the index of the old problem and a new problem object
    public void EditProblem(int index, Problem e) {
        problemArray.set(index, e);
        sortArray();
    }

    /**
     * Returns number of problems in ProblemList
     * */
    public int getListCount() {
        return problemArray.size();
    }

    /**
     * Returns problem at a given index
     * @param index index of problem you want to return
     * */
    public Problem getElementByIndex(int index) {
        return problemArray.get(index);

    }
    /**
     * Removes problem at a given index
     * @param index index of problem you want to remove
     * */
    public void removeProblemFromList(int index) {
        problemArray.remove(index);
        sortArray();
    }


    /**
     * Sorts array by date from recent to least recent
     * */
    public void sortArray() {
        Collections.sort(problemArray, new Comparator<Problem>() {
            @Override
            public int compare(Problem t1, Problem t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
    }


    public void sortRecordArray(int i) {
        Collections.sort(problemArray.get(i).recordArray, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    public void addToRecordToProblem(int index, Record record) {
        problemArray.get(index).recordArray.add(record);
    }


    public void addRecordListToProblem(int position, ArrayList<Record> recordList) {
        Log.d("IVANLIM", problemArray.get(position).getTitle());
        problemArray.get(position).recordArray = new ArrayList<>(recordList);
        for (Record r : problemArray.get(position).recordArray) {
            Log.d("IVANLIM", r.getTitle());
        }
    }

    public ArrayList<Record> getRecordList(int position) {
        sortRecordArray(position);
        return problemArray.get(position).recordArray;
    }

    public void removeRecord(int problemPosition, int recordPosition) {
        problemArray.get(problemPosition).recordArray.remove(recordPosition);
        sortRecordArray(problemPosition);
    }

    public void addRecord(int problemPosition, Record r) {
        problemArray.get(problemPosition).recordArray.add(r);
        sortRecordArray(problemPosition);
    }

    public Record getRecord(int problemPosition, int recordPosition) {
        return problemArray.get(problemPosition).recordArray.get(recordPosition);
    }

}
