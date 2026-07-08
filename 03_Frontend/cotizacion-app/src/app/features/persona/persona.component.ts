import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Persona } from './models/persona.model';
import { PersonaService } from './services/persona.service';

@Component({
  selector: 'app-persona',
  imports: [],
  templateUrl: './persona.component.html'
})
export class PersonaComponent implements OnInit {

  personas: Persona[] = [];

  constructor(
    private personaService: PersonaService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.personaService.getPersonasTodas().subscribe({
      next: (data) => {
        this.personas = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching personas', err)
    });
  }
}
