var shaders = Vars.mods.locateMod("深暗之地").root.child("shaders");
var s1 = new Shaders.SurfaceShader(Shaders.getShaderFi("screenspace.vert").readString(), shaders.child("lowtemp.frag").readString())
var m = new CacheLayer.ShaderLayer(s1)
CacheLayer.add(m);
let fallbluePool = extend(Floor, "低温液池", {});
fallbluePool.cacheLayer = m;