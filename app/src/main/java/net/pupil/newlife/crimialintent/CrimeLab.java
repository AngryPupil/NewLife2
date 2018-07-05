package net.pupil.newlife.crimialintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import net.pupil.newlife.crimialintent.database.CrimeBaseHelper;
import net.pupil.newlife.crimialintent.database.CrimeCursorWrapper;
import net.pupil.newlife.crimialintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Angry on 2018/2/24.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        /*
            此处使用context.getApplicationContext()的原因：
            要回答上述问题，关键就在于考虑它们的生命周期。只要有activity在， Android肯定也创建
            有application对象。用户在应用的不同界面间导航时，各个activity时而存在时而消亡，但application
            对象不会受任何影响。可以说，它的生命周期要比任何activity都要长。
            CrimeLab是个单例。这表明，一旦创建，它就会一直存在直至整个应用进程被销毁。由代码
            可知， CrimeLab引用着mContext对象。显然，如果把activity作为mContext对象保存的话，这个
            由CrimeLab一直引用着的activity肯定会免遭垃圾回收器的清理，即便用户跳转离开这个activity
            时也是如此。
            为了避免资源浪费，我们使用了应用程序上下文。这样， CrimeLab仍可以引用Context对象，
            而activity的存在和消亡也不用受它束缚了。
         */
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mSQLiteDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuid = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);
        mSQLiteDatabase.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + "=?", new String[]{uuid});
    }

    public CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                Crime crime = cursorWrapper.getCrime();
                crimes.add(crime);
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + "=?", new String[]{id.toString()});
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

//    public int getIndexById(UUID id) {
//        return -1;
//    }

    public ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved());
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    public File getPhotoFile(Crime crime) {
        File dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (dir == null) {
            return null;
        }
        return new File(dir, crime.getPhotoFileName());
    }

}
