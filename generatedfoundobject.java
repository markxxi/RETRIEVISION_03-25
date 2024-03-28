package com.example.retrievision;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.app.TimePickerDialog;
import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class generatedfoundobject extends AppCompatActivity {
    public ChipGroup chipType, chipColor, chipModel, chipBrand, chipSize, chipText, chipUF, chipLocation;
    public Button addTypeButton, addColorButton, addModelButton, addBrandButton, addSizeButton, addTextButton, addUFButton, addLocationButton;
    public Typeface typeface;
    public Chip tags;
    public EditText editTextTime, editTextDate;
    String[] item = {"Ground Floor", "1st Floor", "2nd FLoor (Mezzanine)", "2nd Floor (Faculty)", "3rd Floor", "4th Floor (Multipurpose Hall)", "4th Floor (Library)", "Roof Deck", "Restroom", "Court"};
    String[] category = {"Personal Belongings", "Jewelry", "Electronics", "Clothing", "Stationaries", "Documents"};
    AutoCompleteTextView autoCompleteTextView, autoCompleteCategory;
    ArrayAdapter<String> adapterItems, locationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatedfoundobject);

        //get all xml values
        //for type
        chipType = findViewById(R.id.chipType);
        addTypeButton = findViewById(R.id.addTypeButton);
        //for color
        chipColor = findViewById(R.id.chipColor);
        addColorButton = findViewById(R.id.addColorButton);
        //for model and brand | no size
        chipModel = findViewById(R.id.chipModel);
        chipBrand = findViewById(R.id.chipBrand);
        addModelButton = findViewById(R.id.addModelButton);
        addBrandButton = findViewById(R.id.addBrandButton);
        //size
        chipSize = findViewById(R.id.chipSize);
        addSizeButton = findViewById(R.id.addSizeButton);
        //text and UF
        chipText = findViewById(R.id.chipText);
        addTextButton = findViewById(R.id.addTextButton);
        chipUF = findViewById(R.id.chipUF);
        addUFButton = findViewById(R.id.addUFButton);

        chipLocation = findViewById(R.id.chipLocation);
        addLocationButton = findViewById(R.id.locationButton);
        //
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate = findViewById(R.id.editTextDate);


        //initialization
        typeFirstChip();
        colorFirstChip();
        modelFirstChip();
        brandFirstChip();
        sizeFirstChip();
        textFirstChip();
        UFFirstChip();


        dropdownFunction();
        locationDropdownFunction();

        editTextTime.setFocusable(false);
        editTextDate.setFocusable(false);
        //if dropdown should not show the list when clicked, set false to focusable
        //add button function for type should be here | or not???

    }

    //important!!!! in getting index, should i remove dropdown and add button inside chipgroup?
    public void dropdownFunction() {
        autoCompleteTextView = findViewById(R.id.locationDropdown);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        //  Chip addalocation = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        //  chipDesign(addalocation);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterItems.getItem(i);
                Chip newChip = createChip(selectedItem);
                chipLocation.addView(newChip);
            }
        });
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFunction();
                ;
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFunction();
                ;
            }
        });
    }

    private Chip createChip(String text) {
        Chip chip = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        chip.setText(text);
        chipDesign(chip);
        return chip;
    }

    public void locationDropdownFunction() {
        autoCompleteCategory = findViewById(R.id.categoryDropdown);
        locationItems = new ArrayAdapter<String>(this, R.layout.list_item, category);
        autoCompleteCategory.setAdapter(locationItems);
    }

    //FUNCTION FOR EACH DESCRIPTION
    private void typeFirstChip() {
        Intent intent = getIntent();
        if (intent != null) {
            String data = intent.getStringExtra("highestObject");

            //inflates chip
            Chip firstType = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
            typeface = Typeface.createFromAsset(getAssets(), "poppinsmedium.ttf");
            firstType.setId(View.generateViewId());
            firstType.setText(data);
            firstType.setTypeface(typeface);
            chipDesign(firstType);

            //this makes the first chip of type
            int index = chipType.indexOfChild(addTypeButton);
            chipType.addView(firstType, index);

            addTypeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewNextChip(chipType);
                    int index = chipType.indexOfChild(addTypeButton);
                    chipType.addView(tags, index);
                }
            });

            firstType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editChip(firstType);
                }
            });

        } else {
            Log.e("Error", "Type is Null");
        }
    }
    public void colorFirstChip() {
        Intent intent = getIntent();
        String colorString = intent.getStringExtra("dominant_colors");
        if (colorString != null) {
            String[] colorsArray = colorString.split("\n");

            for (String color : colorsArray) {
                Chip firstColor = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);

                firstColor.setId(View.generateViewId());
                firstColor.setText(color);

                //firstColor.setChipBackgroundColorResource (int id);
                //firstColor.setText("Color");
                chipDesign(firstColor);
                int index = chipColor.indexOfChild(addColorButton);
                chipColor.addView(firstColor, index);//the number of colors can be adjusted in py code

                firstColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editChip(firstColor);
                    }
                });
            }
        } else {
            Log.e("Error", "Dominant colors extra not found.");
        }

        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNextChip(chipColor);
                int index = chipColor.indexOfChild(addColorButton);
                chipColor.addView(tags, index);
            }
        });
    }
    public void modelFirstChip() {
        Chip firstModel = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        firstModel.setText("Model");
        chipDesign(firstModel);

        int index = chipModel.indexOfChild(addModelButton);
        chipModel.addView(firstModel, index);
        addModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNextChip(chipModel);
                int index = chipModel.indexOfChild(addModelButton);
                chipModel.addView(tags, index);
            }
        });
        firstModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChip(firstModel);
            }
        });
    }
    public void brandFirstChip() {
        Chip firstBrand = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        firstBrand.setText("Brand");
        chipDesign(firstBrand);
        int index = chipBrand.indexOfChild(addBrandButton);
        chipBrand.addView(firstBrand, index);
        addBrandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNextChip(chipBrand);
                int index = chipBrand.indexOfChild(addBrandButton);
                chipBrand.addView(tags, index);
            }
        });
        firstBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChip(firstBrand);
            }
        });
    }
    public void sizeFirstChip() {
        Chip firstSize = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        firstSize.setText("Size");
        chipDesign(firstSize);
        int index = chipSize.indexOfChild(addSizeButton);
        chipSize.addView(firstSize, index);
        addSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNextChip(chipSize);
                int index = chipSize.indexOfChild(addSizeButton);
                chipSize.addView(tags, index);
            }
        });
        firstSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChip(firstSize);
            }
        });
    }
    public void textFirstChip() {
        Chip firstText = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        firstText.setText("Text");
        chipDesign(firstText);
        int index = chipText.indexOfChild(addTextButton);
        chipText.addView(firstText, index);
        addTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNextChip(chipText);
                int index = chipText.indexOfChild(addTextButton);
                chipText.addView(tags, index);
            }
        });
        firstText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChip(firstText);
            }
        });
    }

    public void UFFirstChip() {
        Chip firstUF = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        firstUF.setText("Unique Feature");
        chipDesign(firstUF);

        addUFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNextChip(chipUF);
                int index = chipUF.indexOfChild(addUFButton);
                chipUF.addView(tags, index);
            }
        });
        firstUF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChip(firstUF);
            }
        });
    }

    //all chips will inherit this ----------------------------------------------------------------------------------------

    public void timeFunction() {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(generatedfoundobject.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeFormat;
                if (hourOfDay >= 12) {
                    timeFormat = "PM";
                    if (hourOfDay > 12) {
                        hourOfDay -= 12;
                    }
                } else {
                    timeFormat = "AM";
                    if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                }
                editTextTime.setText(hourOfDay + ":" + minute + " " + timeFormat);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
    public void dateFunction() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(generatedfoundobject.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String date = dateFormat.format(c.getTime());
                editTextDate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private void addNewNextChip(final ChipGroup chipGroup) {
        tags = (Chip) LayoutInflater.from(this).inflate(R.layout.chip_item, null, false);
        tags.setId(View.generateViewId());
        updateChipText(tags, chipGroup);
        tags.setText("Add a Tag");
        chipDesign(tags);

        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChip(tags);
            }
        });
        int childCount = chipGroup.getChildCount() - 1;
        tags.setText("Add a Tag (" + childCount + ")");
    }

    private void updateChipText(Chip chip, ChipGroup chipGroup) {
        int childCount = chipGroup.getChildCount();
        chip.setText("Add a Tag (" + childCount + ")");
    }

    private void editChip(Chip chip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_chip_dialog, null);
        final EditText editTextChipText = dialogView.findViewById(R.id.editTextChipText);
        Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
        editTextChipText.setText(chip.getText());
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = editTextChipText.getText().toString();
                chip.setText(newText);
                dialog.dismiss();
            }
        });
    }
    public void chipDesign(Chip chip) {
        chip.setTypeface(typeface);
        //color
        chip.setTextColor(getResources().getColor(R.color.fadedblue));
        chip.setChipMinHeight(90);
    }
}