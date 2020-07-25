package net.pupil.newlife.career;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import net.pupil.newlife.R;
import net.pupil.newlife.pupilsuper.PupilActivity;

import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerActivity extends PupilActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        List<ObjectGroup> groups = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ObjectGroup group = new ObjectGroup("n_" + i, new ArrayList<ObjectChild>());
            for (int j = 10; j < 18; j++) {
                ObjectChild child = new ObjectChild();
                child.mName = "num_" + j;
                group.getItems().add(child);
            }
            groups.add(group);
        }
        Log.d(TAG, groups.toString());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PupilExpandableRecyclerAdapter(groups));
    }

    static class PupilExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<SimpleGroupViewHolder, SimpleChildViewHolder> {

        public PupilExpandableRecyclerAdapter(List<ObjectGroup> list) {
            super(list);
        }

        @Override
        public SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new SimpleGroupViewHolder(view);
        }

        @Override
        public SimpleChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new SimpleChildViewHolder(view);
        }

        @Override
        public void onBindGroupViewHolder(SimpleGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
            ObjectGroup og = (ObjectGroup) group;
            holder.mTextView.setText(og.getTitle());
        }

        @Override
        public void onBindChildViewHolder(SimpleChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            ObjectChild child = (ObjectChild) group.getItems().get(childIndex);
            holder.mTextView.setText(child.mName);
        }
    }

    static class SimpleGroupViewHolder extends GroupViewHolder {

        TextView mTextView;

        public SimpleGroupViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    static class SimpleChildViewHolder extends ChildViewHolder {

        TextView mTextView;

        public SimpleChildViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    static class ObjectGroup extends ExpandableGroup<ObjectChild> {

        public ObjectGroup(String title, List<ObjectChild> items) {
            super(title, items);
        }
    }

    static class ObjectChild implements Parcelable {

        public String mName;

        public ObjectChild() {
        }

        protected ObjectChild(Parcel in) {
            mName = in.readString();
        }

        public static final Creator<ObjectChild> CREATOR = new Creator<ObjectChild>() {
            @Override
            public ObjectChild createFromParcel(Parcel in) {
                return new ObjectChild(in);
            }

            @Override
            public ObjectChild[] newArray(int size) {
                return new ObjectChild[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        @Override
        public String toString() {
            return "ObjectChild{" +
                    "mName='" + mName + '\'' +
                    '}';
        }
    }


}
