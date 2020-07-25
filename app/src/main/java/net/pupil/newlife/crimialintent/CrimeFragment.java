package net.pupil.newlife.crimialintent;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import net.pupil.newlife.R;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Yu on 2017/3/13.
 */
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private static final String DIALOG_PHOTO_BITMAP = "dialog_photo_bitmap";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_SUSPECT = 1;
    private static final int READ_CONTACTS_REQUEST_CODE = 2;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_VIEW_PHOTO = 4;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private String mContactId;
    private ImageView mCrimePhoto;
    private ImageButton mCrimeCamera;
    private File mPhotoFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
//                returnResult();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mCrime.getDate());
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(calendar);
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
//                returnResult();
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
//                startActivity(intent);

                ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
                intentBuilder.setType("text/plain");
                intentBuilder.setText(getCrimeReport());
                intentBuilder.setSubject(getString(R.string.crime_report_subject));
                intentBuilder.setChooserTitle(getString(R.string.send_report));
                startActivity(intentBuilder.getIntent());
            }
        });

        final Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //故意添加类别给Intent，使系统找不到匹配的Activity
//        intent.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQUEST_SUSPECT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        //判断是否有可用的应用
        final PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        mCrimePhoto = (ImageView) v.findViewById(R.id.crime_photo);
        mCrimeCamera = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePic = mPhotoFile != null && cameraIntent.resolveActivity(packageManager) != null;
        mCrimeCamera.setEnabled(canTakePic);
        if (canTakePic) {
            Uri uri = Uri.fromFile(mPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mCrimeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        });
        mCrimePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoFile == null || !mPhotoFile.exists()) {
                    return;
                }
                PhotoDialogFragment dialogFragment = PhotoDialogFragment.newInstance(getScaledBitmap());
                dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_VIEW_PHOTO);
                dialogFragment.show(getFragmentManager(), DIALOG_PHOTO);
            }
        });
        ViewTreeObserver treeObserver = mCrimePhoto.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhotoView();
            }
        });

        return v;
    }

    private Bitmap getScaledBitmap() {
        return PhotoUtils.getScaledBitmap(mPhotoFile.getPath(), mCrimePhoto);
    }

    public static class PhotoDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
            ImageView photoImageView = (ImageView) v.findViewById(R.id.crime_photo_image_view);
            photoImageView.setImageBitmap((Bitmap) getArguments().getParcelable(DIALOG_PHOTO_BITMAP));
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.crime_photo)
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismiss();
                        }
                    })
                    .create();
        }

        public static PhotoDialogFragment newInstance(Bitmap bitmap) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DIALOG_PHOTO_BITMAP, bitmap);
            PhotoDialogFragment photoDialogFragment = new PhotoDialogFragment();
            photoDialogFragment.setArguments(bundle);
            return photoDialogFragment;

        }
    }


    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE,MMM dd,yyyy", mCrime.getDate()));
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

//    private void returnResult() {
//        Intent intent = CrimeListFragment.newIntent(CrimeLab.get(getActivity()).getIndexById(mCrime.getId()));
//        getActivity().setResult(Activity.RESULT_OK, intent);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
//            returnResult();
        } else if (requestCode == REQUEST_SUSPECT) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                if (c == null || c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                mContactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
                } else {
                    dialContactPhone();
                }
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_CAMERA) {
            updatePhotoView();
        }
    }

    private void dialContactPhone() {
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] phoneQueryFields = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection  = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] selectionArgs = new String[]{mContactId};
        Cursor phoneCursor = getActivity().getContentResolver().query(phoneUri, phoneQueryFields, selection, selectionArgs, null);
        try {
            if (phoneCursor == null || phoneCursor.getCount() == 0) {
                return;
            }
            phoneCursor.moveToFirst();
            String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Uri numUri = Uri.parse("tel:" + phoneNum);
            Intent intent = new Intent(Intent.ACTION_DIAL, numUri);
            startActivity(intent);
        } finally {
            phoneCursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialContactPhone();
                }
            }
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mCrimePhoto.setImageDrawable(null);
        } else {
            mCrimePhoto.setImageBitmap(getScaledBitmap());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }
}
