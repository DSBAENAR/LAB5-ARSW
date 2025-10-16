var App = (function(){
    let blueprints = []
    let currentAuthor = "";
    let currentBlueprint = null;
    let api = apiclient
    let isNewBlueprint = false;

    return {
         /** Draw the specified blueprint on the canvas and set it as currentBlueprint. */
       drawBlueprint: function(author,bpname){
        api.getBlueprintsByNameAndAuthor(author,bpname,function(bp){
            if (!bp){
                alert("Blueprint not found")
                return
            }
            $("#currentBlueprint").text(bp.name);
            currentBlueprint = bp;
            let canvas = document.getElementById("blueprintCanvas");
            let ctx = canvas.getContext("2d");

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            if (bp.points && bp.points.length > 0) {
                ctx.beginPath();
                ctx.moveTo(bp.points[0].x, bp.points[0].y);

                for (let i = 1; i < bp.points.length; i++) {
                    ctx.lineTo(bp.points[i].x, bp.points[i].y);
                }

                ctx.stroke();
            }

            if(bp.points){
                ctx.fillStyle = 'red';
                for(let p of bp.points){
                    ctx.beginPath();
                    ctx.arc(p.x,p.y,3,0,2*Math.PI);
                    ctx.fill();
                }
            }
        })
       },

       /**
        * Initialize canvas event handlers to capture clicks/taps (pointer/mouse/touch).
        * canvasId: id of the canvas element (e.g. 'blueprintCanvas')
        */
       initCanvas: function(canvasId){
           const canvas = document.getElementById(canvasId);
           if(!canvas) {
               console.warn('Canvas not found:', canvasId);
               return;
           }
           const ctx = canvas.getContext('2d');

           function getCanvasRelativePos(evt){
               const rect = canvas.getBoundingClientRect();
               let clientX = 0, clientY = 0;

               if (evt.touches && evt.touches.length > 0) {
                   clientX = evt.touches[0].clientX;
                   clientY = evt.touches[0].clientY;
               }
               else if (evt.changedTouches && evt.changedTouches.length > 0) {
                   clientX = evt.changedTouches[0].clientX;
                   clientY = evt.changedTouches[0].clientY;
               }
               else if (typeof evt.clientX === 'number' && typeof evt.clientY === 'number') {
                   clientX = evt.clientX;
                   clientY = evt.clientY;
               }
               else if (evt.touches && evt.touches[0]){
                   clientX = evt.touches[0].clientX || 0;
                   clientY = evt.touches[0].clientY || 0;
               }

               return {
                   x: Math.round(clientX - rect.left),
                   y: Math.round(clientY - rect.top)
               };
           }
           /** Draw a small red circle at (x,y) on the canvas */
           function drawMarker(x,y){
               ctx.save();
               ctx.fillStyle = 'red';
               ctx.beginPath();
               ctx.arc(x,y,3,0,2*Math.PI);
               ctx.fill();
               ctx.restore();
           }

           /** Handle pointer/mouse/touch event: add point to current blueprint (if any),
            * draw it on canvas and update total points counter.
            */
           function handlePointer(e){
               if(e.preventDefault) e.preventDefault();
               const pos = getCanvasRelativePos(e);
               console.log('Canvas click at:', pos);

               if(!currentBlueprint){
                   console.log('No blueprint selected; ignoring canvas click.');
                   return;
               }

               if(!currentBlueprint.points) currentBlueprint.points = [];
               currentBlueprint.points.push({x: pos.x, y: pos.y});

               ctx.clearRect(0,0,canvas.width,canvas.height);
               if(currentBlueprint.points.length > 0){
                   ctx.beginPath();
                   ctx.moveTo(currentBlueprint.points[0].x, currentBlueprint.points[0].y);
                   for(let i=1;i<currentBlueprint.points.length;i++){
                       ctx.lineTo(currentBlueprint.points[i].x, currentBlueprint.points[i].y);
                   }
                   ctx.stroke();
               }
               ctx.fillStyle = 'red';
               for(let p of currentBlueprint.points){
                   ctx.beginPath();
                   ctx.arc(p.x,p.y,3,0,2*Math.PI);
                   ctx.fill();
               }

               const $tp = $('#totalPoints');
               const current = parseInt($tp.text()) || 0;
               $tp.text(current + 1);
           }

           if(window.PointerEvent){
               canvas.addEventListener('pointerdown', handlePointer);
           } else {
               canvas.addEventListener('touchstart', function(e){ handlePointer(e); }, {passive:false});
               canvas.addEventListener('mousedown', handlePointer);
           }

           canvas.clearMarkers = function(){
               ctx.clearRect(0,0,canvas.width,canvas.height);
           };
       },

        /** Update the blueprint table for the specified author by querying the API.
         * Also updates the total points counter for that author.
         */
        updateBlueprints: function(author){
            api.getBlueprintsByAuthor(author, function(bps){

            let transformed = bps.map(bp => ({
                name: bp.name,
                points: bp.points.length
            }));

           
            let $table = $("#blueprintTable");
            $table.empty(); 
            $table.append(`
                <tr>
                    <th>Blueprint Name</th>
                    <th>Number of points</th>
                </tr>
            `);

            transformed.map(bp => {
                let row = `<tr>
                            <td>${bp.name}</td>
                            <td>${bp.points}</td>
                            <td><button onclick="App.drawBlueprint('${author}', '${bp.name}')">Draw</button></td>

                        </tr>`;
                $table.append(row);
            });

          
            let totalPoints = transformed.reduce((acc, bp) => acc + bp.points, 0);

            
            $("#totalPoints").text(totalPoints);
        });
        },

        setBlueprints : function(blprnts){
            blueprints = blprnts
        },

        getAuthor: function(){
            return currentAuthor
        },

        getBlueprints: function(){
            return blueprints
        }
        ,

        /**
         * Save (PUT) the currently opened blueprint to the API, then refresh all blueprints
         * and update totals for the current author.
         */
        saveCurrentBlueprint: function(){
            if(!currentBlueprint){
                alert('No blueprint selected to save.');
                return;
            }
            const author = currentBlueprint.author;
            const name = currentBlueprint.name;

            if(isNewBlueprint){
                api.postBlueprint(currentBlueprint, function(postErr, postResp){
                    if(postErr){
                        alert('Error creating blueprint: ' + postErr);
                        return;
                    }
                    isNewBlueprint = false;
                    api.getAllBlueprints(function(getErr, data){
                        if(getErr){
                            alert('Error retrieving blueprints after create: ' + getErr);
                            return;
                        }
                        blueprints = data;
                        const authorBps = data.filter(b => b.author === author);
                        const totalPoints = authorBps.reduce((acc,bp) => acc + (bp.points?bp.points.length:0), 0);
                        $('#totalPoints').text(totalPoints);
                        try{ if($('#inputFindAuthor').val() === author){ App.updateBlueprints(author); } }catch(e){}
                    });
                });
            } else {
                api.putBlueprint(author, name, currentBlueprint, function(err, resp){
                    if(err){
                        alert('Error saving blueprint: ' + err);
                        return;
                    }
                    api.getAllBlueprints(function(getErr, data){
                        if(getErr){
                            alert('Error retrieving blueprints after save: ' + getErr);
                            return;
                        }
                        blueprints = data;
                        const authorBps = data.filter(b => b.author === author);
                        const totalPoints = authorBps.reduce((acc,bp) => acc + (bp.points?bp.points.length:0), 0);
                        $('#totalPoints').text(totalPoints);
                        try{ if($('#inputFindAuthor').val() === author){ App.updateBlueprints(author); } }catch(e){}
                    });
                });
            }
        },

        /**
         * Create a new blueprint: clears canvas, prompts for name (and author if missing),
         * prepares a new currentBlueprint in memory and marks it as new (first Save will POST).
         */
        createNewBlueprint: function(){
            const canvas = document.getElementById('blueprintCanvas');
            if(canvas){
                const ctx = canvas.getContext('2d');
                ctx.clearRect(0,0,canvas.width,canvas.height);
            }

            let author = $('#inputFindAuthor').val();
            if(!author){
                author = prompt('Enter author name for the new blueprint:');
                if(!author) return; 
                $('#inputFindAuthor').val(author);
            }

            const name = prompt('Enter name for the new blueprint:');
            if(!name) return; 

            currentBlueprint = { author: author, name: name, points: [] };
            isNewBlueprint = true;
            $('#currentBlueprint').text(name);

            api.getAllBlueprints(function(getErr, data){
                if(getErr){ console.warn('Could not refresh blueprints after create init:', getErr); return; }
                blueprints = data;
                const authorBps = data.filter(b => b.author === author);
                const totalPoints = authorBps.reduce((acc,bp) => acc + (bp.points?bp.points.length:0), 0);
                $('#totalPoints').text(totalPoints);
                // refresh table if author selected
                try{ if($('#inputFindAuthor').val() === author){ App.updateBlueprints(author); } }catch(e){}
            });
        }
    }
})();

window.addEventListener('load', function(){
    try{
        App.initCanvas('blueprintCanvas');
    }catch(err){
        console.warn('Failed to init canvas handlers', err);
    }
});





