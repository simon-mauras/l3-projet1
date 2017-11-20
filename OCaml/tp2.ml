let fibo n =
  let rec fibo_aux = function
    | 0 -> (0, 1)
    | u -> let a, b = fibo_aux (u-1) in (b, a+b) in
  fst (fibo_aux n);;

(*
let a = int_of_string (read_line ());;

(* Marche aussi :
let a = read_int ();; *)

print_int (fibo a);;
print_newline ();;
*)

type couleur =
  | Rouge
  | Jaune
  | Vert
  | Bleu;;

let est_couleur_primaire = function
  | Vert -> false
  |  _   -> true;;

type nombre =
  | Int of int
  | Real of float;;

type liste =
  | Liste_vide
  | Cons of (int*liste);;

(* On choisis d'autoriser la somme entre un entier et un flottant *)
let somme a b = match a, b with
  | Int(x), Int(y)   -> Int(x + y)
  | _ -> let u = (match a with Int(x)  -> float_of_int x
                             | Real(x) -> x) in
	 let v = (match b with Int(y)  -> float_of_int y
                             | Real(y) -> y) in
	 Real(u +. v);;

somme (Int(1)) (Int(3));;
somme (Real(1.)) (Int(3));;

let rec longueur = function
  | Liste_vide -> 0
  | Cons(_, l) -> 1 + longueur l;;

longueur (List.fold_left (fun l x -> Cons(x, l)) Liste_vide [1; 2; 4; 5; 6; 7]);;

let au_moins_deux = function
  | [] | [_] -> false
  | t1::t2::q -> true;;

(*let au_moins_deux = function
  | [] | [a] -> false
  | t1::t2::q -> true;;

a doit être définie pour tout les cas du pattern matching
(même si on ne l'utilise pas...) *)

let rec extremum = function
  | [] -> failwith "Liste vide..."
  | [a] -> (a, a)
  | a::l -> let mini, maxi = extremum l in
	    (min mini a, max maxi a);;

extremum [1; 6; 1000; -100001; 4567; 8];;

let deuxieme lst =
  let rec aux = function
  | [] | [_] -> failwith "Liste trop petite..."
  | a::[b] -> (min a b, max a b)
  | a::l -> let u, v = aux l in
	    if a > v
	      then (v, a)
	    else if a > u
	      then (a, v)
	      else (u, v) in
  fst (aux lst);;

deuxieme [1; 6; 1000; -100001; 4567; 8];;

let rec map f = function
  | [] -> []
  | a::l -> (f a)::(map f l);;

map (fun x -> x + 2) [4; 7; -5; 2; 0];;
map fibo [2; 3; 4; 5; 6];;

let rec tri_insertion lst =
  let rec insere x = function
    | [] -> [x]
    | a::l -> if x < a then x::a::l
                       else a::(insere x l) in
  match lst with
    | [] -> []
    | a::l -> insere a (tri_insertion l);;

tri_insertion [-6; 2; -10; 9; 0; 2];;

let rec tri_selection lst =
  let rec select_min = function
    | [] -> failwith "Impossible"
    | [a] -> (a, [])
    | a::l -> let x, y = select_min l in
	      if a < x then (a, l)
	               else (x, a::y) in
  if lst <> []
    then let x,l = select_min lst in x::(tri_selection l)
    else [];;

tri_selection [-6; 3; 8; -10; 0; 10; -1; 0];;

let rec tri_rapide lst =
  let rec coupe_pivot x = function
    | [] -> ([], [])
    | a::l -> let u, v = coupe_pivot x l in
	      if a < x then (a::u, v) else (u, a::v) in
  let rec concat l1 l2 = match l1 with
    | [] -> l2
    | a::l -> a::(concat l l2) in
  match lst with
    | [] -> []
    | a::l -> let u, v = coupe_pivot a l in
	      concat (tri_rapide u) (a::(tri_rapide v));;

tri_rapide [10; -3; 0; 1000; 7; 0; -3; 10];;
tri_rapide (List.init 1000 (fun i -> i));;

(* On arrive mieux à ajouter des elements au debut,
du coup on choisit la fonction descend_min... *)
let rec tri_bulle lst =
  (* let rec remonte_max = function
    | [] -> []
    | [x] -> [x]
    | a::b::l -> if a > b then b::(remonte_max (a::l))
                          else a::(remonte_max (b::l)) in *)
  let rec descend_min = function
    | [] -> []
    | [x] -> [x]
    | a::l0 -> (match descend_min l0 with
	        | b::l -> if a > b then b::a::l
                                   else a::b::l
                | _ -> failwith "Impossible") in
  if lst = []
  then []
  else match descend_min lst with
        | a::l -> a::(tri_bulle l)
	| _ -> failwith "Impossible";;

tri_bulle [10 -3; 10; 4; 100; 0; -8; -100];;

let rec listeDecroissante = function
  | 0 -> []
  | n -> n::(listeDecroissante (n-1));;

let listeCroissante n = List.rev (listeDecroissante n);;

let rec listeAleatoire = function
  | 0 -> []
  | n -> (Random.int 1000000000)::(listeAleatoire (n-1));;

let l1 = listeAleatoire 1000;;
let l2 = List.sort (-) l1;;

l2 = tri_bulle l1;;
l2 = tri_insertion l1;;
l2 = tri_selection l1;;
l2 = tri_rapide l1;;

let l3 = listeAleatoire 100000;;
let l4 = List.sort (-) l3;;
let l5 = tri_rapide l3;;

l4 = l5;;

tri_rapide (listeCroissante 10000);; (* Mouarf *)

let rec pgcd a b = match a, b with
  | 0, x -> x
  | u, v -> pgcd (v mod u) u;;

pgcd (101*77) (2*5*77);;

let rec composition_repetee f = function
  | 0 -> (fun x -> x)
  | n -> (fun x -> (composition_repetee f (n-1)) (f x));;

map (fun n -> composition_repetee (fun x -> 2*x) n 1) [1; 2; 3; 4; 5];;
(* Renvoie la liste des puissance de 2 associées *)
(* Parce que pour chaque element n de la liste,
on applique n fois la fonction *2 a partir de la valeur 1*)
