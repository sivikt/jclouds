/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.openstack.swift;

import java.io.Closeable;
import java.util.Map;
import java.util.Set;

import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.http.options.GetOptions;
import org.jclouds.openstack.swift.domain.AccountMetadata;
import org.jclouds.openstack.swift.domain.ContainerMetadata;
import org.jclouds.openstack.swift.domain.MutableObjectInfoWithMetadata;
import org.jclouds.openstack.swift.domain.ObjectInfo;
import org.jclouds.openstack.swift.domain.SwiftObject;
import org.jclouds.openstack.swift.options.CreateContainerOptions;
import org.jclouds.openstack.swift.options.ListContainerOptions;

import com.google.inject.Provides;

/**
 * Common features in OpenStack Swift.
 * 
 * @author Adrian Cole
 * 
 * @deprecated Please use {@code com.jclouds.openstack.swift.v1.SwiftApi} and related
 *             feature APIs in {@code com.jclouds.openstack.swift.v1.features.*} as noted in
 *             each method. This interface will be removed in jclouds 2.0.
 */
@Deprecated
public interface CommonSwiftClient extends Closeable {
   
   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.domain.SwiftObject#builder()}
    */
   @Provides
   SwiftObject newSwiftObject();

   /**
    * HEAD operations against an identity are performed to retrieve the number of Containers and the
    * total bytes stored in Cloud Files for the identity.
    * <p/>
    * Determine the number of Containers within the identity and the total bytes stored. Since the
    * storage system is designed to store large amounts of data, care should be taken when
    * representing the total bytes response as an integer; when possible, convert it to a 64-bit
    * unsigned integer if your platform supports that primitive flavor.
    * 
    * @return the {@link AccountMetadata}
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.AccountApi#get()}
    */
   AccountMetadata getAccountStatistics();

   /**
    * GET operations against the X-Storage-Url for an identity are performed to retrieve a list of
    * existing storage
    * <p/>
    * Containers ordered by name. The following list describes the optional query parameters that
    * are supported with this request.
    * <ul>
    * <li>limit - For an integer value N, limits the number of results to at most N values.</li>
    * <li>marker - Given a string value X, return Object names greater in value than the specified
    * marker.</li>
    * <li>format - Specify either json or xml to return the respective serialized response.</li>
    * </ul>
    * <p/>
    * At this time, a prex query parameter is not supported at the Account level.
    * 
    *<h4>Large Container Lists</h4>
    * The system will return a maximum of 10,000 Container names per request. To retrieve subsequent
    * container names, another request must be made with a marker parameter. The marker indicates
    * where the last list left off and the system will return container names greater than this
    * marker, up to 10,000 again. Note that the marker value should be URL encoded prior to sending
    * the HTTP request.
    * <p/>
    * If 10,000 is larger than desired, a limit parameter may be given.
    * <p/>
    * If the number of container names returned equals the limit given (or 10,000 if no limit is
    * given), it can be assumed there are more container names to be listed. If the container name
    * list is exactly divisible by the limit, the last request will simply have no content.
    * 
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#list()} and
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#list(ListContainerOptions)}
    */
   Set<ContainerMetadata> listContainers(ListContainerOptions... options);

   /**
    * Get the {@link ContainerMetadata} for the specified container.
    * 
    * @param container
    *           the container to get the metadata from
    * @return the {@link ContainerMetadata}
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#get()}
    */
   ContainerMetadata getContainerMetadata(String container);
   
   /**
    * Set the {@link ContainerMetadata} on the given container.
    * 
    * @param container
    *           the container to set the metadata on
    * @param containerMetadata
    *           a {@code Map<String, String>} containing the metadata
    * @return {@code true}
    *            if the Container Metadata was successfully created or updated, false if not.
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#updateMetadata()}
    */
   boolean setContainerMetadata(String container, Map<String, String> containerMetadata);
   
   /**
    * Delete the metadata on the given container.
    * 
    * @param container
    *           the container to delete the metadata from
    * @param metadataKeys
    *           the metadata keys
    * @return {@code true}
    *            if the Container was successfully deleted, false if not.
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#deleteMetadata()}
    */
   boolean deleteContainerMetadata(String container, Iterable<String> metadataKeys);

   /**
    * Create a container.
    * 
    * @param container
    *           the name of the container
    * @return {@code true}
    *            if the Container was successfully created, false if not.
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#createIfAbsent()}
    */
   boolean createContainer(String container);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#createIfAbsent()}
    */
   boolean createContainer(String container, CreateContainerOptions... options);
   
   /**
    * @deprecated This method will be replaced by
    *             (@link com.jclouds.openstack.swift.v1.features.ContainerApi#deleteIfEmpty()}
    */
   boolean deleteContainerIfEmpty(String container);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ContainerApi#head()}
    */
   boolean containerExists(String container);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#list()} and
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#list(ListContainerOptions)}
    */
   PageSet<ObjectInfo> listObjects(String container, ListContainerOptions... options);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#get()}
    */
   SwiftObject getObject(String container, String name, GetOptions... options);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi@updateMetadata()}
    */
   boolean setObjectInfo(String container, String name, Map<String, String> userMetadata);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#head()}
    */
   MutableObjectInfoWithMetadata getObjectInfo(String container, String name);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#replace()}
    */
   String putObject(String container, SwiftObject object);

   /**
    * @return True If the object was copied
    * @throws CopyObjectException If the object was not copied
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#copy()}
    */
   boolean copyObject(String sourceContainer, String sourceObject, String destinationContainer, String destinationObject);
   
   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#delete()}
    */
   void removeObject(String container, String name);

   /**
    * @throws org.jclouds.blobstore.ContainerNotFoundException
    *            if the container is not present
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#head()}
    */
   boolean objectExists(String container, String name);

   /**
    * @deprecated This method will be replaced by
    *             {@link com.jclouds.openstack.swift.v1.features.ObjectApi#replaceManifest()}
    */
   String putObjectManifest(String container, String name);
}
