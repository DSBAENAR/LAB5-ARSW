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





