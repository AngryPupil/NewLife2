package net.pupil.newlife.expandablelistview;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import net.pupil.newlife.R;

public class ExpandableActivity extends AppCompatActivity {

    private String[][] items = new String[][]{{"1","2"},{"1.1","1.2","1.3"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);

        ExpandableListAdapter adapter = new MyExpandableListAdapter();
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable);
        expandableListView.setAdapter(adapter);
    }

    private class MyExpandableListAdapter implements ExpandableListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return items.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return items[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return items[groupPosition][0];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return items[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return LayoutInflater.from(ExpandableActivity.this).inflate(android.R.layout.two_line_list_item, null);
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(ExpandableActivity.this).inflate(android.R.layout.simple_expandable_list_item_1, null);
            TextView tv = (TextView) v.findViewById(android.R.id.text1);
            tv.setText(items[groupPosition][childPosition]);
            return v;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }
    }
}
