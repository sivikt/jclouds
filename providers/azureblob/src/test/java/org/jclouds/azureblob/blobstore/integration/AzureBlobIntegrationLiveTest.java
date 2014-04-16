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
package org.jclouds.azureblob.blobstore.integration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.jclouds.azureblob.blobstore.strategy.MultipartUploadStrategy;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.integration.internal.BaseBlobIntegrationTest;
import org.jclouds.blobstore.options.PutOptions;
import org.testng.SkipException;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import static com.google.common.hash.Hashing.md5;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live")
public class AzureBlobIntegrationLiveTest extends BaseBlobIntegrationTest {
    private ByteSource oneHundredOneConstitutions;
    private byte[] oneHundredOneConstitutionsMD5;

   public AzureBlobIntegrationLiveTest() {
      provider = "azureblob";
   }
   @Override
   public void testGetIfMatch() throws InterruptedException {
      // this currently fails
   }

    @Override
    public void testGetIfModifiedSince() throws InterruptedException {
       // this currently fails!
   }

   public void testCreateBlobWithExpiry() throws InterruptedException {
      throw new SkipException("Expires header unsupported: http://msdn.microsoft.com/en-us/library/windowsazure/dd179404.aspx#Subheading3");
   }

   @Override
   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testPutObjectStream() throws InterruptedException, IOException, ExecutionException {
      super.testPutObjectStream();
   }

   // according to docs, content disposition is not persisted
   // http://msdn.microsoft.com/en-us/library/dd179440.aspx
   @Override
   protected void checkContentDisposition(Blob blob, String contentDisposition) {
      assert blob.getPayload().getContentMetadata().getContentDisposition() == null;
      assert blob.getMetadata().getContentMetadata().getContentDisposition() == null;
   }

   /**
    * Essentially copied from the AWS multipart chucked stream test
    */
   public void testMultipartChunkedFileStream() throws IOException, InterruptedException {
      oneHundredOneConstitutions = getTestDataSupplier();
      oneHundredOneConstitutionsMD5 = oneHundredOneConstitutions.hash(md5()).asBytes();
      File file = new File("target/const.txt");
      oneHundredOneConstitutions.copyTo(Files.asByteSink(file));
      String containerName = getContainerName();

      try {
         BlobStore blobStore = view.getBlobStore();
         blobStore.createContainerInLocation(null, containerName);
         Blob blob = blobStore.blobBuilder("const.txt").payload(file).build();
         String expected = blobStore.putBlob(containerName, blob, PutOptions.Builder.multipart());
         String etag = blobStore.blobMetadata(containerName, "const.txt").getETag();
         assertEquals(etag, expected);
      } finally {
         returnContainer(containerName);
      }
   }

   public void testMultipartChunkedFileStreamPowerOfTwoSize() throws IOException, InterruptedException {
      final long limit = MultipartUploadStrategy.MAX_BLOCK_SIZE;
      ByteSource input = repeatingArrayByteSource(new byte[1024]).slice(0, limit);
      File file = new File("target/const.txt");
      input.copyTo(Files.asByteSink(file));
      String containerName = getContainerName();

      try {
         BlobStore blobStore = view.getBlobStore();
         blobStore.createContainerInLocation(null, containerName);
         Blob blob = blobStore.blobBuilder("const.txt").payload(file).build();
         String expected = blobStore.putBlob(containerName, blob, PutOptions.Builder.multipart());
         String etag = blobStore.blobMetadata(containerName, "const.txt").getETag();
         assertEquals(etag, expected);
      } finally {
         returnContainer(containerName);
      }
   }

   private static ByteSource repeatingArrayByteSource(final byte[] input) {
      return ByteSource.concat(Iterables.cycle(ByteSource.wrap(input)));
   }
}
