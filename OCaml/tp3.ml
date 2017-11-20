(* Exercice 1 *)

type arbre = Vide | Noeud of int * arbre * arbre ;;

let rec taille = function
  | Vide -> 0
  | Noeud(_, a, b) -> 1 + (taille a) + (taille b);;  

let rec nb_feuilles = function
  | Vide -> 0
  | Noeud(_, Vide, Vide) -> 1
  | Noeud(_, a, b) -> (nb_feuilles a) + (nb_feuilles b);;

let rec hauteur = function
  | Vide -> 0
  | Noeud(_, a, b) -> 1 + max (hauteur a) (hauteur b);;

let rec detect arb n = match arb with
  | Vide -> false
  | Noeud(x, a, b) -> x == n || (detect a n) || (detect b n);;
  
let complet arb =
  let rec aux = function
    | Vide -> (0, 0)
    | Noeud(_, a, b) -> let min1, max1 = aux a in
                        let min2, max2 = aux b in
               (1 + min min1 min2, 1 + max max1 max2) in
  let mini, maxi = aux arb in mini = maxi;;

let arbre_to_list arb = 
  let rec aux l = function
  | Vide -> l
  | Noeud(x, a, b) -> aux (x::(aux l b)) a in
  aux [] arb;;

let rec tri_fusion l = 
  let rec split a b = function
    | [] -> a, b
    | u::v -> split b (u::a) v in
  let rec fusionne a b = match a, b with
    | [], _  -> b
    | _ , [] -> a
    | x::u, y::v -> if x < y
                      then x::(fusionne u b)
                      else y::(fusionne a v) in
  match l with
    | [] | [_] -> l
    | _ -> let l1, l2 = split [] [] l in
           fusionne (tri_fusion l1) (tri_fusion l2);;


(* La fonction construire produit un arbre presque complet
en ayant pour parametre la liste des etiquettes restantes,
sa taille et la profondeur maximum. *)
let arbre_to_abr arb =
  let liste = tri_fusion (arbre_to_list arb) in
  let rec construire lst h n =
    if lst = [] then Vide, lst, n
    else if h = 0 then Vide, lst, n
    else if n = 0 then Vide, lst, n
    else let a1, l1, n1 = construire lst (h-1) (n-1) in
	 let x = List.hd l1 in
	 let a2, l2, n2 = construire (List.tl l1) (h-1) n1 in
	 Noeud(x, a1, a2), l2, n2 in
  let rec logint = function
    | 0 -> 0
    | n -> 1 + logint (n/2) in
  let taille = List.length liste in
  let res, _, _ = construire liste (logint taille) taille in
  res;;
			      

let rec recherche_abr x = function
  | Vide -> false
  | Noeud(a, g, d) -> if a = x then true
                      else if x < a
                        then recherche_abr x g
                        else recherche_abr x d;;

let arbre1 = Noeud(10, Noeud(5, Noeud(3, Vide, Vide), Noeud(1, Vide, Vide)), Noeud(4, Noeud(12, Vide, Vide), Noeud(4, Vide, Vide)));;
let abr1 = arbre_to_abr arbre1;;

taille abr1;;
hauteur abr1;;
nb_feuilles abr1;;
arbre_to_list abr1;;
recherche_abr 8 abr1;;
recherche_abr 12 abr1;;

let rec listeAleatoire = function
  | 0 -> []
  | n -> (Random.int 1000000000)::(listeAleatoire (n-1));;

let arbre2 = List.fold_left (fun a x -> Noeud(x, a, Vide)) Vide (listeAleatoire 32);;
let abr2 = arbre_to_abr arbre2;;

arbre_to_list abr2;;

let valide n =
  let x = List.fold_left (fun a x -> Noeud(x, a, Vide))
                         Vide (listeAleatoire n) in
  let y = arbre_to_abr x in
  (taille y, hauteur y,
   (tri_fusion (arbre_to_list x)) = arbre_to_list y);;

List.map valide [1; 2; 3; 7; 8; 9; 1023; 1024; 1025];;

(* Exercice 2 *)
let swap a b =
  let c = !a in
  a := !b;
  b := c;;

let mystere () =
  let n = Random.int 101 in
  for i=1 to 7 do
    let m = read_int () in
    if m < n then print_endline "Plus petit...";
    if m > n then print_endline "Plus grand !!";
    if m = n then print_endline "BRAVO =)";
  done;;

let chance () =
  let a = Array.init 10 (fun _ -> Random.int 11) in
  if Array.fold_left (||) false (Array.mapi (=) a)
  then print_endline "Bravo =)"
  else print_endline "Tu es nul !";;

chance ();;

(* Exercice 3 *)
type etudiant = {nom : string;
		 prenom : string;
		 mutable notes : int list};;

let ajoute e n = e.notes <- n::e.notes;;

let moyenne e =
  let somme = List.fold_left (+) 0 e.notes in
  let nb = List.length e.notes in
  (float_of_int somme) /. (float_of_int nb);;

(* L'objectif est de retirer le minimum en un seul parcours,
on calcul a l'aller et on retire au retour... *)
let triche e =
  let rec aux m = function
    | [] -> [], false, m
    | a::l -> let lst, b, mini = aux (min a m) l in
	      if b then a::lst, true, mini
	      else if a = mini then l, true, mini
	      else a::l, false, mini in
  let res, _, _ = aux 1000000000 e.notes in
  e.notes <- res;;

let moyenne_classe e =
  let somme = List.fold_left (fun x y -> x + List.hd (y.notes)) 0 e in
  let nb = List.length e in
  (float_of_int somme) /. (float_of_int nb);;
  

let etudiant1 = {nom = "Dupond"; prenom = "John";
		 notes = [2; 4; 19; 1; 20; 8]};;

triche etudiant1;;
etudiant1;;
