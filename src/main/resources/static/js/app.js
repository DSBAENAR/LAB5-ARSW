var App = (function(){
    let blueprints = []
    let currentAuthor = "";
    let currentBlueprint = null; // currently displayed blueprint (in-memory)
    let api = apiclient

    return {
       drawBlueprint: function(author,bpname){
        api.getBlueprintsByNameAndAuthor(author,bpname,function(bp){
            if (!bp){
                alert("Blueprint not found")
                return
            }
            $("#currentBlueprint").text(bp.name);
            // store the currently opened blueprint in memory
            currentBlueprint = bp;
            let canvas = document.getElementById("blueprintCanvas");
            let ctx = canvas.getContext("2d");

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // draw lines
            if (bp.points && bp.points.length > 0) {
                ctx.beginPath();
                ctx.moveTo(bp.points[0].x, bp.points[0].y);

                for (let i = 1; i < bp.points.length; i++) {
                    ctx.lineTo(bp.points[i].x, bp.points[i].y);
                }

                ctx.stroke();
            }

            // draw markers for points
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

               // Touch events (mobile)
               if (evt.touches && evt.touches.length > 0) {
                   clientX = evt.touches[0].clientX;
                   clientY = evt.touches[0].clientY;
               }
               // Some browsers use changedTouches for the end of touch
               else if (evt.changedTouches && evt.changedTouches.length > 0) {
                   clientX = evt.changedTouches[0].clientX;
                   clientY = evt.changedTouches[0].clientY;
               }
               // Mouse/Pointer events
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

           function drawMarker(x,y){
               ctx.save();
               ctx.fillStyle = 'red';
               ctx.beginPath();
               ctx.arc(x,y,3,0,2*Math.PI);
               ctx.fill();
               ctx.restore();
           }

           function handlePointer(e){
               // Prevent default to avoid synthetic mouse events after touch
               if(e.preventDefault) e.preventDefault();
               const pos = getCanvasRelativePos(e);
               console.log('Canvas click at:', pos);

               // Only add point if there's a current blueprint selected
               if(!currentBlueprint){
                   console.log('No blueprint selected; ignoring canvas click.');
                   return;
               }

               // add the new point to the blueprint in memory (at the end)
               if(!currentBlueprint.points) currentBlueprint.points = [];
               currentBlueprint.points.push({x: pos.x, y: pos.y});

               // repaint: clear canvas and redraw lines + markers
               ctx.clearRect(0,0,canvas.width,canvas.height);
               if(currentBlueprint.points.length > 0){
                   ctx.beginPath();
                   ctx.moveTo(currentBlueprint.points[0].x, currentBlueprint.points[0].y);
                   for(let i=1;i<currentBlueprint.points.length;i++){
                       ctx.lineTo(currentBlueprint.points[i].x, currentBlueprint.points[i].y);
                   }
                   ctx.stroke();
               }
               // draw markers
               ctx.fillStyle = 'red';
               for(let p of currentBlueprint.points){
                   ctx.beginPath();
                   ctx.arc(p.x,p.y,3,0,2*Math.PI);
                   ctx.fill();
               }

               // update total points displayed (increment by 1)
               const $tp = $('#totalPoints');
               const current = parseInt($tp.text()) || 0;
               $tp.text(current + 1);
           }

           // Use PointerEvent if available
           if(window.PointerEvent){
               canvas.addEventListener('pointerdown', handlePointer);
           } else {
               // Touch fallback
               canvas.addEventListener('touchstart', function(e){ handlePointer(e); }, {passive:false});
               // Mouse fallback
               canvas.addEventListener('mousedown', handlePointer);
           }

           // expose a clear function for convenience
           canvas.clearMarkers = function(){
               ctx.clearRect(0,0,canvas.width,canvas.height);
           };
       },

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
    }
})();

// Auto-initialize canvas handlers when page loads
window.addEventListener('load', function(){
    try{
        App.initCanvas('blueprintCanvas');
    }catch(err){
        console.warn('Failed to init canvas handlers', err);
    }
});





