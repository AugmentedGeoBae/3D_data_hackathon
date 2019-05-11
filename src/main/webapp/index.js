
        var viewer;
        var options = {
            env: 'AutodeskProduction',
            accessToken: 'eyJhbGciOiJIUzI1NiIsImtpZCI6Imp3dF9zeW1tZXRyaWNfa2V5In0.eyJjbGllbnRfaWQiOiJzSkFHcmJVVlRnbEhLRzlJR3UwNUFhbU9uVDJnWGliVCIsImV4cCI6MTU1NzU5MjA1Miwic2NvcGUiOlsiZGF0YTpjcmVhdGUiLCJkYXRhOndyaXRlIiwiZGF0YTpyZWFkIiwiYnVja2V0OnJlYWQiLCJidWNrZXQ6dXBkYXRlIiwiYnVja2V0OmNyZWF0ZSJdLCJhdWQiOiJodHRwczovL2F1dG9kZXNrLmNvbS9hdWQvand0ZXhwNjAiLCJqdGkiOiIxOEk3VUVjVndodno5S2dWT080cHhaVFJaOXBuTGRwSnZEb0NCRVVwZzd2eEhJaXNQR01DaXFzUlBKMDhrUUZSIn0.Do_-kAm_sHgJtoeGE3ZF7qN4cf3HZXmp51m-HLJ-ODI'
        };
        var documentId = 'urn:dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6aGFjazNkZHVibGluL2hhY2tkdWJsaW4uemlw';
        Autodesk.Viewing.Initializer(options, function onInitialized(){
            Autodesk.Viewing.Document.load(documentId, onDocumentLoadSuccess, onDocumentLoadFailure);
        });

        /**
        * Autodesk.Viewing.Document.load() success callback.
        * Proceeds with model initialization.
        */
        function onDocumentLoadSuccess(doc) {

            // A document contains references to 3D and 2D viewables.
            var viewables = Autodesk.Viewing.Document.getSubItemsWithProperties(doc.getRootItem(), {'type':'geometry'}, true);
            if (viewables.length === 0) {
                console.error('Document contains no viewables.');
                return;
            }

            // Choose any of the avialble viewables
            var initialViewable = viewables[0];
            var svfUrl = doc.getViewablePath(initialViewable);
            var modelOptions = {
                sharedPropertyDbPath: doc.getPropertyDbPath()
            };

            var viewerDiv = document.getElementById('MyViewerDiv');
            viewer = new Autodesk.Viewing.Private.GuiViewer3D(viewerDiv);
            viewer.start(svfUrl, modelOptions, onLoadModelSuccess, onLoadModelError);
        }

        /**
         * Autodesk.Viewing.Document.load() failuire callback.
         */
        function onDocumentLoadFailure(viewerErrorCode) {
            console.error('onDocumentLoadFailure() - errorCode:' + viewerErrorCode);
        }

        /**
         * viewer.loadModel() success callback.
         * Invoked after the model's SVF has been initially loaded.
         * It may trigger before any geometry has been downloaded and displayed on-screen.
         */
        function onLoadModelSuccess(model) {
            console.log('onLoadModelSuccess()!');
            console.log('Validate model loaded: ' + (viewer.model === model));
            console.log(model);
        }

        /**
         * viewer.loadModel() failure callback.
         * Invoked when there's an error fetching the SVF file.
         */
        function onLoadModelError(viewerErrorCode) {
            console.error('onLoadModelError() - errorCode:' + viewerErrorCode);
        }

        var riskBlob = {

          "type": 1,
          "description": 1,
          "status": 1,
          "date": 1

        };

        var incidentBlob1 = {};

        var personBlob = {};

        var equipmentBlob = {};

        function incidentBlob(x, y, z, blobColour, info) {

          var blobGeom = new THREE.SphereGeometry(5, 10, 10);
          var blobMat = new THREE.MeshBasicMaterial({color: blobColour});
          var blobMesh = new THREE.Mesh(blobGeom, blobMat);
          blobMesh.position.set(x,y,z);

          blobMesh.addEventListener(); //onclick ..... );

          oViewer.impl.scene.add(blobMesh);
          oViewer.impl.sceneUpdated(true);

        }
