/*
 * Copyright 2003-2005 The Apache Software Foundation.
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

package org.apache.struts.chain.commands;


import org.apache.struts.chain.contexts.ActionContext;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ModuleConfig;


/**
 * <p>Cache the <code>ActionConfig</code> instance for the
 * action to be used for processing this request.</p>
 *
 * @version $Rev$ $Date$
 */

public abstract class AbstractSelectAction extends ActionCommandBase {




    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Cache the <code>ActionConfig</code> instance for the
     * action to be used for processing this request.</p>
     *
     * @param actionCtx The <code>Context</code> for the current request
     *
     * @exception IllegalArgumentException if no valid
     *  action can be identified for this request
     *
     * @return <code>false</code> so that processing continues
     */
    public boolean execute(ActionContext actionCtx) throws Exception {

        // Identify the matching path for this request
        String path = getPath(actionCtx);

        // Cache the corresponding ActonConfig instance
        ModuleConfig moduleConfig = actionCtx.getModuleConfig();
        ActionConfig actionConfig = moduleConfig.findActionConfig(path);

        if (actionConfig == null) {
            //:TODO Shouldn't this be the responsibility of ModuleConfig?
            // Locate the mapping for unknown paths (if any)
            ActionConfig configs[] = moduleConfig.findActionConfigs();
            for (int i = 0; i < configs.length; i++) {
                if (configs[i].getUnknown()) {
                    actionConfig = configs[i];
                    break;
                }
            }

        }

        if (actionConfig == null) {
            throw new InvalidPathException(
                        "No action config found for the specified url.", path);
        }
        actionCtx.setActionConfig(actionConfig);
        return (false);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return the path to be used to select the <code>ActionConfig</code>
     * for this request.</p>
     *
     * @param context The <code>Context</code> for this request
     *
     * @exception IllegalArgumentException if no valid
     *  action can be identified for this request
     */
    protected abstract String getPath(ActionContext context);


}
