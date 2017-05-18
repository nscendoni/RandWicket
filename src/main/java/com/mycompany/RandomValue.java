/*
 * Copyright 2017 nicola.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycompany;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 *
 * @author nicola
 */
public class RandomValue extends Label {

    public RandomValue(String id) {
        super(id, new RandomModel());
    }

    private static class RandomModel extends AbstractReadOnlyModel<String> {

        BufferedInputStream in;
        StringBuilder buffer = new StringBuilder();

        public RandomModel() {
            try {
                Process p = Runtime.getRuntime().exec("/home/nicola/tmp/rand.sh");
                in = new BufferedInputStream(p.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(RandomValue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
         */
        @Override
        public String getObject() {

            try {
                if (in.available() > 0) {
                    int kar = in.read();
                    if (kar != -1) {
                        while (kar != -1 && kar != (int) '\n') {
                            buffer.append((char) kar);
                            kar = in.read();
                        }
                        buffer.append("\n");
                        return buffer.toString();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(RandomValue.class.getName()).log(Level.SEVERE, null, ex);
            }
            return buffer.toString();
        }
    }

}
