package com.example.sixsapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sixsapp.R;
import com.example.sixsapp.pojo.Answer;
import com.example.sixsapp.pojo.Pair;
import com.example.sixsapp.pojo.Question;
import com.example.sixsapp.utilis.Global;

import java.util.List;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {
    public Context context;
    public Activity activity;
    public List<Pair> pairList;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public EasyImage easyImage;

    public QuestionnaireAdapter(Activity activity, Context context, List<Pair> pairList) {
        this.activity = activity;
        this.context = context;
        this.pairList = pairList;
        this.easyImage = new EasyImage.Builder(context)
                .setChooserTitle("Pick media")
//                .setCopyImagesToPublicGalleryFolder(true) // THIS requires granting WRITE_EXTERNAL_STORAGE permission for devices running Android 9 or lower
//                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("SixS App")
                .allowMultiple(false)
                .build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dummy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Pair pair = pairList.get(position);
        Question question = pair.getQuestion();
        Answer answer = pair.getAnswer();

        holder.mQuestion.setText(question.getQuestion());

        if(answer.getImageUris()[0] != null){
            Glide.with(context)
                    .load(answer.getImageUris()[0])
                    .centerCrop()
                    .into(holder.mImageAttachment0);
        } else {
            holder.mImageAttachment0.setImageResource(R.drawable.add_image_placeholder);
        }
        if(answer.getImageUris()[1] != null){
            Glide.with(context)
                    .load(answer.getImageUris()[1])
                    .centerCrop()
                    .into(holder.mImageAttachment1);
        } else {
            holder.mImageAttachment1.setImageResource(R.drawable.add_image_placeholder);
        }
        if(answer.getImageUris()[2] != null) {
            Glide.with(context)
                    .load(answer.getImageUris()[2])
                    .centerCrop()
                    .into(holder.mImageAttachment2);
        } else {
            holder.mImageAttachment2.setImageResource(R.drawable.add_image_placeholder);
        }
        if(answer.getImageUris()[3] != null){
            Glide.with(context)
                    .load(answer.getImageUris()[3])
                    .centerCrop()
                    .into(holder.mImageAttachment3);
        } else {
            holder.mImageAttachment3.setImageResource(R.drawable.add_image_placeholder);
        }
        if(answer.getImageUris()[4] != null){
            Glide.with(context)
                    .load(answer.getImageUris()[4])
                    .centerCrop()
                    .into(holder.mImageAttachment4);
        } else {
            holder.mImageAttachment4.setImageResource(R.drawable.add_image_placeholder);
        }

        holder.mImageAttachment0.setOnClickListener(view -> {
            easyImage.openCameraForImage(activity);
            Global.PICTURE_NO = 0;
            Global.ITEM_POSITION = position;
        });

        holder.mImageAttachment1.setOnClickListener(view -> {
            easyImage.openCameraForImage(activity);
            Global.PICTURE_NO = 1;
            Global.ITEM_POSITION = position;
        });

        holder.mImageAttachment2.setOnClickListener(view -> {
            easyImage.openCameraForImage(activity);
            Global.PICTURE_NO = 2;
            Global.ITEM_POSITION = position;
        });

        holder.mImageAttachment3.setOnClickListener(view -> {
            easyImage.openCameraForImage(activity);
            Global.PICTURE_NO = 3;
            Global.ITEM_POSITION = position;
        });

        holder.mImageAttachment4.setOnClickListener(view -> {
            easyImage.openCameraForImage(activity);
            Global.PICTURE_NO = 4;
            Global.ITEM_POSITION = position;
        });

//        holder.mScore.setText(String.valueOf(answer.getScore()));
//
//        holder.mScore.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                try {
//                    int holderPosition = holder.getAdapterPosition();
//                    //holderPosition == position
//                    //This Condition solved the Fucking issue of EditText Value Changed
//                    if (holderPosition != RecyclerView.NO_POSITION && holderPosition == position) {
//                        int newScore = Integer.parseInt(charSequence.toString());
//                        answer.setScore(newScore);
//                        holder.mScore.clearFocus();
//                    }
//                } catch (NumberFormatException e) {
//                    // Handle invalid input
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {}
//        });

        int radioButtonId = getRadioButtonIdForScore(answer.getFindings());
        holder.mRadioGroup.check(radioButtonId);

        holder.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int holderPosition = holder.getAdapterPosition();
                if (holderPosition != RecyclerView.NO_POSITION && holderPosition == position) {
                    RadioButton selectedRadioButton = holder.itemView.findViewById(checkedId);
                    String selectedValue = selectedRadioButton.getText().toString();
                    setScoreToRadio(holderPosition, Integer.parseInt(selectedValue));

                }
            }
        });

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) holder.mSpinner.getAdapter();
        int index = adapter.getPosition(answer.getRemarks());

        if (index != -1) {
            holder.mSpinner.setSelection(index);
        } else {
            holder.mSpinner.setSelection(0);
        }

        holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int holderPosition = holder.getAdapterPosition();
                if (holderPosition != RecyclerView.NO_POSITION && holderPosition == position) {
                    String selectedValue = adapterView.getItemAtPosition(i).toString();
                    if(i != 0){
                        answer.setRemarks(selectedValue);
                    } else {
                        answer.setRemarks("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setSelectedImageUri(int position, Uri uri, int pictureNumber) {
        if (position != RecyclerView.NO_POSITION) {
            Pair pair = pairList.get(position);
            Uri[] uris = pair.getAnswer().getImageUris();
            uris[pictureNumber] = uri;
            pair.getAnswer().setImageUris(uris);
            pairList.set(position, pair);
            notifyItemChanged(position);
        }
    }

    public void setScoreToRadio(int position, int score) {
        if (position != RecyclerView.NO_POSITION) {
            Pair pair = pairList.get(position);
            pair.getAnswer().setFindings(score);
            pairList.set(position, pair);
            //notifyItemChanged(position);
        }
    }

    private int getRadioButtonIdForScore(int score) {
        switch (score) {
            case 0:
                return R.id.radio0;
            case 1:
                return R.id.radio1; // Replace with the actual ID of your radio button
            case 2:
                return R.id.radio2;
            case 3:
                return R.id.radio3;
            case 4:
                return R.id.radio4;
            case 5:
                return R.id.radio5;
            case 6:
                return R.id.radio6;
            default:
                return R.id.radio0; // Return a default value or handle other cases
        }
    }

    @Override
    public int getItemCount() {
        return pairList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextInputEditText mScore;
        TextView mQuestion;
        ImageView mImageAttachment0, mImageAttachment1, mImageAttachment2, mImageAttachment3, mImageAttachment4;
        RadioGroup mRadioGroup;
        AppCompatSpinner mSpinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            mScore = itemView.findViewById(R.id.score_Input);
            mQuestion = itemView.findViewById(R.id.question_textView);
            mImageAttachment0 = itemView.findViewById(R.id.attachmentImage_1);
            mImageAttachment1 = itemView.findViewById(R.id.attachmentImage_2);
            mImageAttachment2 = itemView.findViewById(R.id.attachmentImage_3);
            mImageAttachment3 = itemView.findViewById(R.id.attachmentImage_4);
            mImageAttachment4 = itemView.findViewById(R.id.attachmentImage_5);
            mRadioGroup = itemView.findViewById(R.id.radio_group);
            mSpinner = itemView.findViewById(R.id.spinner);
        }

    }

}
