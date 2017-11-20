#load "graphics.cma"

let x = Graphics.open_graph "";;

(* Cette fonction trace une ligne brisée (modifié pour ne pas partir de 0...) *)
let rec draw l = match l with
  | [] -> failwith "Liste vide..."
  | [x0, y0] -> Graphics.moveto x0 y0;
  | (x0, y0)::l1 -> draw l1;
                    Graphics.lineto x0 y0;
                    Graphics.moveto x0 y0;;

let damier m n =
  for i = 0 to m-1 do
    for j = 0 to n-1 do
      if (i + j) mod 2 = 0
      then Graphics.fill_rect ((i*Graphics.size_x ())/m) ((j*Graphics.size_y ())/n)
			      ((Graphics.size_x ())/m) ((Graphics.size_y ())/n);
    done;
  done;;

damier 5 4;;

(*draw [(100, 150); (200, 200)];;*)
(*draw [(150, 150); (250, 200)];;*)

let rec koch = function
  | [] -> []
  | [a] -> [a]
  | (x0, y0)::(x4, y4)::l ->
    let x1 = (2.*.x0 +. x4) /. 3. in
    let y1 = (2.*.y0 +. y4) /. 3. in
    let x3 = (x0 +. 2.*.x4) /. 3. in
    let y3 = (y0 +. 2.*.y4) /. 3. in
    let xm = (x0 +. x4) /. 2. in
    let ym = (y0 +. y4) /. 2. in
    let x2 = xm -. (ym -. y1) *. sqrt 3. in
    let y2 = ym +. (xm -. x1) *. sqrt 3. in
    (x0, y0)::(x1, y1)::(x2, y2)::(x3, y3)::(koch ((x4, y4)::l));;

let rec composition_repetee f = function
  | 0 -> (fun x -> x)
  | n -> (fun x -> (composition_repetee f (n-1)) (f x));;

let lf = composition_repetee koch 5
         [(100., 120.); (300., 450.); (500., 120.); (100., 120.)];;
let li = List.map (fun (a, b) -> (int_of_float a, int_of_float b)) lf;;

draw li;;
