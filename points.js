var data = require("./sampleData")
var proj4 = require("./proj4")

var convert = (data) => {
    // Source data coordinate system conversion
    proj4.defs("EPSG:2157","+proj=tmerc +lat_0=53.5 +lon_0=-8 +k=0.99982 +x_0=600000 +y_0=750000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs");
    
    var source = new proj4.Proj('EPSG:4326') // Source coordinates will be in Longitude/Latitude
    var dest = new proj4.Proj('EPSG:2157') // ITM
    var latX = data.lon
    var latY = data.lat
    var projRes = proj4(source, dest, [latX, latY])
    var offsetX = 716777978.214865 // ?
    var offsetY = 734238937.834393 // ? 
    var offsetZ = 12871.66658984362 // ?
    var lonOffset = projRes[0] - offsetX // ?
    var latOffset = projRes[1] - offsetY // ?

    // Sample values from console.log(NOP_VIEWER.impl.camera , but Position, Target, Up values need to be complete for each view.
    var camera = {
      fov: 53.13010235415598,
      isPerspective: true,
      orthoScale: 6.442020414517138,
      position: { // Position of camera tbc
        x: 1,
        y: 1,
        z: 1
      },
      target: { // Position of sphere tbc
        x: 1,
        y: 1,
        z: 1
      },
      up: { // Up vector for camera tbc
        x: 1,
        y: 1,
        z: 1
      }
    }
    return camera
}

const mapper = issues => {
  const newIssue = {};
  Object.keys(issues).forEach(key => {
    newIssue[key] = {
      ...issues[key],
      ...convert(issues[key])
    };
  })
  return newIssue;
}

console.log(mapper(data));
