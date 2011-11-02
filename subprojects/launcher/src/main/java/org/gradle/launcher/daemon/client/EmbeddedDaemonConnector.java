/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.launcher.daemon.client;

import org.gradle.api.internal.project.ServiceRegistry;
import org.gradle.api.specs.Spec;
import org.gradle.launcher.daemon.registry.EmbeddedDaemonRegistry;
import org.gradle.launcher.daemon.context.DaemonContext;
import org.gradle.launcher.daemon.context.DaemonContextFactory;
import org.gradle.launcher.daemon.server.Daemon;
import org.gradle.launcher.daemon.server.DaemonServerConnector;
import org.gradle.launcher.daemon.server.DaemonTcpServerConnector;
import org.gradle.launcher.daemon.server.exec.DefaultDaemonCommandExecuter;
import org.gradle.messaging.remote.internal.OutgoingConnector;
import org.gradle.messaging.concurrent.DefaultExecutorFactory;

/**
 * A daemon connector that starts daemons by launching new daemons in the same jvm.
 */
public class EmbeddedDaemonConnector extends DaemonConnectorSupport<EmbeddedDaemonRegistry> {

    private final ServiceRegistry loggingServices;
    private final DaemonContext daemonContext = new DaemonContextFactory().create();

    public EmbeddedDaemonConnector(EmbeddedDaemonRegistry daemonRegistry, Spec<DaemonContext> contextCompatibilitySpec, OutgoingConnector<Object> outgoingConnector, ServiceRegistry loggingServices) {
        super(daemonRegistry, contextCompatibilitySpec, outgoingConnector);
        this.loggingServices = loggingServices;
    }

    protected void startDaemon() {
        EmbeddedDaemonRegistry daemonRegistry = getDaemonRegistry();

        DaemonServerConnector server = new DaemonTcpServerConnector();
        DefaultExecutorFactory executorFactory = new DefaultExecutorFactory();
        daemonRegistry.startDaemon(new Daemon(server, daemonRegistry, daemonContext, new DefaultDaemonCommandExecuter(loggingServices, executorFactory), executorFactory));
    }

}
