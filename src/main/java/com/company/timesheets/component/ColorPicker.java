package com.company.timesheets.component;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;

//https://vaadin.com/docs/latest/flow/create-ui/element-api

@Tag("input")
public class ColorPicker extends AbstractSinglePropertyField<ColorPicker, String> {

    public ColorPicker() {
        super("value", "", false);

        getElement().setAttribute("type", "color");

        setSynchronizedEvent("change");
    }

}
