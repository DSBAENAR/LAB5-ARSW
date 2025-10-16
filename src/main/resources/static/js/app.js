var App = (function(){
    let blueprints = []
    let currentAuthor = "";
    let api = apiclient

    return {
       drawBlueprint: function(author,bpname){
        api.getBlueprintsByNameAndAuthor(author,bpname,function(bp){
            if (!bp){
                alert("Blueprint not found")
                return
            }
            $("#currentBlueprint").text(bp.name);
            let canvas = document.getElementById("blueprintCanvas");
            let ctx = canvas.getContext("2d");

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            if (bp.points.length > 0) {
                ctx.beginPath();
                ctx.moveTo(bp.points[0].x, bp.points[0].y);

                for (let i = 1; i < bp.points.length; i++) {
                    ctx.lineTo(bp.points[i].x, bp.points[i].y);
                }

                ctx.stroke();
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
               let clientX, clientY;
               if(evt instanceof TouchEvent){
                   clientX = evt.touches[0].clientX;
                   clientY = evt.touches[0].clientY;
               } else if(evt instanceof PointerEvent || evt instanceof MouseEvent){
                   clientX = evt.clientX;
                   clientY = evt.clientY;
               } else {
                   clientX = evt.clientX || (evt.touches && evt.touches[0] && evt.touches[0].clientX) || 0;
                   clientY = evt.clientY || (evt.touches && evt.touches[0] && evt.touches[0].clientY) || 0;
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
               drawMarker(pos.x,pos.y);

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





